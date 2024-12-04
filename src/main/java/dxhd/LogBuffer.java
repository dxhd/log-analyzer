package dxhd;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class LogBuffer {

    private final BlockingQueue<LogEntry> logQueue;

    public LogBuffer(int bufferSize) {
        logQueue = new ArrayBlockingQueue<>(bufferSize);
    }

    public void put(LogEntry log) {
        try {
            logQueue.put(log);
        } catch (InterruptedException e) {
            throw new RuntimeException(e); //TODO обработать исключение
        }
    }

    public LogEntry poll(long timeout, TimeUnit unit) {
        try {
            return logQueue.poll(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e); //TODO обработать исключение
        }
    }
}
