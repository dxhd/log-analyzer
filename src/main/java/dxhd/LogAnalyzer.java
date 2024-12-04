package dxhd;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LogAnalyzer {

    private final double availabilityThreshold;
    private final double maxResponseTime;
    private final double minIntervalDuration;
    private final LogBuffer logBuffer;

    private TimeInterval currentInterval;

    public LogAnalyzer(double availabilityThreshold, double maxResponseTime, double minIntervalDuration, LogBuffer logBuffer) {
        this.availabilityThreshold = availabilityThreshold;
        this.maxResponseTime = maxResponseTime;
        this.minIntervalDuration = minIntervalDuration;
        this.logBuffer = logBuffer;
    }

    public void analyzeLogs()  {
        while (true) {
            var logEntry = logBuffer.poll(10, TimeUnit.SECONDS);
            if (logEntry == null) {
                break;
            }
            processLogEntry(logEntry);
        }
    }

    public void processLogEntry(LogEntry logEntry) {

        var isFailure = this.isLogFailure(logEntry);

        //  если нет интервала, создаем с первого лога с отказом
        if (currentInterval == null) {
            if (!isFailure) {
                return;
            }
            currentInterval = new TimeInterval();
            currentInterval.setStart(logEntry.getDateTime());
        }

        if (isFailure) {
            processFailureLog(logEntry);
        } else {
            processOkLog(logEntry);
        }
    }

    private void processFailureLog(LogEntry logEntry) {
        currentInterval.incrementFailure();
        currentInterval.incrementTotal();
        var availability = IntervalUtils.calculateAvailability(currentInterval.getSuccessCount(), currentInterval.getTotalCount());

        if (Duration.between(currentInterval.getStart(), logEntry.getDateTime()).toMillis() >= minIntervalDuration) {
            currentInterval.setAvailabilityOnLastFailure(availability);
            currentInterval.setEnd(logEntry.getDateTime());
            currentInterval.setEndOnLastFailure(logEntry.getDateTime());
        }
        currentInterval.setAvailability(availability);
    }

    private void processOkLog(LogEntry logEntry) {
        if (shouldEndInterval()) {
            closeAndPrintInterval();
        } else {
            currentInterval.incrementSuccess();
            currentInterval.incrementTotal();
            currentInterval.setAvailability(IntervalUtils.calculateAvailability(currentInterval.getSuccessCount(), currentInterval.getTotalCount()));

            if (Duration.between(currentInterval.getStart(), logEntry.getDateTime()).toMillis() >= minIntervalDuration) {
                currentInterval.setEnd(logEntry.getDateTime());
            }
        }
    }

    private void closeAndPrintInterval() {
        if (currentInterval.getEndOnLastFailure() != null && currentInterval.getAvailabilityOnLastFailure() != null) {
            currentInterval.setAvailability(currentInterval.getAvailabilityOnLastFailure());
            currentInterval.setEnd(currentInterval.getEndOnLastFailure());
        }
        if (currentInterval.getAvailability() <= availabilityThreshold) {
            IntervalUtils.printInterval(currentInterval);
        }
        currentInterval = null;
    }

    private boolean shouldEndInterval() {
        return currentInterval.getEnd() != null
                && IntervalUtils.calculateAvailability(currentInterval.getSuccessCount() + 1, currentInterval.getTotalCount() + 1) >= availabilityThreshold;
    }

    public Boolean isLogFailure(LogEntry logEntry) {
        return logEntry.getStatusCode().startsWith("4")
                || logEntry.getStatusCode().startsWith("5")
                || logEntry.getResponseTime() > maxResponseTime;
    }
}
