package dxhd;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class LogAnalyzer {

    public static int MIN_INTERVAL_SIZE_IN_MS = 5000;
    public static int BUFFER_SIZE = 10000;

    private final double availabilityThreshold;
    private final long maxResponseTime;

    private final Deque<LogEntry> lookAheadQueue = new ArrayDeque<>();
    private final int lookAheadSize = 10; // Размер окна просмотра

    private TimeInterval currentInterval;

    public LogAnalyzer(double availabilityThreshold, long maxResponseTime) {
        this.availabilityThreshold = availabilityThreshold;
        this.maxResponseTime = maxResponseTime;
    }

    public void analyzeLogs(Stream<LogEntry> logs) {
        logs.forEach(this::processLogEntry);
    }

    public void processLogEntry(LogEntry logEntry) {

        /*lookAheadQueue.offer(logEntry); // Добавляем запись в окно просмотра
        if (lookAheadQueue.size() > lookAheadSize) {
            lookAheadQueue.poll();  // Удаляем старые записи из окна
        }*/

        //  если нет интервала, создаем с первого лога с отказом
        if (currentInterval == null) {
            if (!logEntry.isFailure()) {
                return;
            }
            currentInterval = new TimeInterval();
            currentInterval.setStart(logEntry.getDateTime());
        }

        if (logEntry.isFailure()) {
            currentInterval.incrementFailure();
            currentInterval.incrementTotal();

            var availability = calculateAvailability(currentInterval.getSuccessCount(), currentInterval.getTotalCount());
            if (Duration.between(currentInterval.getStart(), logEntry.getDateTime()).toMillis() >= MIN_INTERVAL_SIZE_IN_MS) {
                currentInterval.setEnd(logEntry.getDateTime());
                currentInterval.setEndOnLastFailure(logEntry.getDateTime());
                currentInterval.setAvailabilityOnLastFailure(availability);
            }
            currentInterval.setAvailability(availability);
        } else {
            if (shouldEndInterval()) {
                if (currentInterval.getEndOnLastFailure() != null && currentInterval.getAvailabilityOnLastFailure() != null) {
                    currentInterval.setAvailability(currentInterval.getAvailabilityOnLastFailure());
                    currentInterval.setEnd(currentInterval.getEndOnLastFailure());
                }
                printInterval(currentInterval);
                currentInterval = null;
            } else {
                currentInterval.incrementSuccess();
                currentInterval.incrementTotal();
                currentInterval.setAvailability(calculateAvailability(currentInterval.getSuccessCount(), currentInterval.getTotalCount()));

                if (Duration.between(currentInterval.getStart(), logEntry.getDateTime()).toMillis() >= MIN_INTERVAL_SIZE_IN_MS) {
                    currentInterval.setEnd(logEntry.getDateTime());
                }
            }
        }
    }

    private boolean shouldEndInterval() {
        return currentInterval.getEnd() != null
                && calculateAvailability(currentInterval.getSuccessCount() + 1, currentInterval.getTotalCount() + 1) >= availabilityThreshold;
    }

    private double calculateAvailability(int successCount, int totalCount) {
        return  ((double) successCount / totalCount) * 100;
    }

    public void printInterval(TimeInterval currentInterval) {
        System.out.println(currentInterval.getStart() + " " + currentInterval.getEnd() + " " + currentInterval.getAvailability());
    }

}
