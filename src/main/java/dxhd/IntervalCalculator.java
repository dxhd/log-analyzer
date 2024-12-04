//package dxhd;
//
//import lombok.Getter;
//
//import java.util.Queue;
//import java.util.concurrent.ArrayBlockingQueue;
//
//@Getter
//public class IntervalCalculator {
//
//    private final Queue<LogEntry> intervalLogs;
//    private final double maxResponseTime;
//    private final int availabilityThreshold;
//    private final int minIntervalSizeInMs;
//
//    public IntervalCalculator(int minIntervalSizeInMs, double maxResponseTime, int availabilityThreshold) {
//        this.minIntervalSizeInMs = minIntervalSizeInMs;
//        this.maxResponseTime = maxResponseTime;
//        this.availabilityThreshold = availabilityThreshold;
//        this.intervalLogs = new ArrayBlockingQueue<>(currentCount);
//    }
//
//    public void add(LogEntry entry) {
//        if (intervalLogs.size() == currentCount) {
//            intervalLogs.poll(); // Удаляем самый старый элемент, если окно заполнено
//        }
//        intervalLogs.offer(entry); // Добавляем новый элемент
//    }
//
//
//    public double calculateAvailability() {
//        if (intervalLogs.isEmpty()) {
//            return 100.0; // Доступность 100%, если окно пустое
//        }
//
//        int failureCount = 0;
//        for (LogEntry entry : intervalLogs) {
//            if (isFailure(entry)) {
//                failureCount++;
//            }
//        }
//
//        return (1.0 - (double) failureCount / intervalLogs.size()) * 100.0;
//    }
//
//
//    private boolean isFailure(LogEntry entry) {
//        return entry.getResponseCode() >= 500 || entry.getResponseTime() > maxResponseTime;
//    }
//
//}
