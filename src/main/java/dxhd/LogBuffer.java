package dxhd;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LogBuffer {

    private final BlockingQueue<LogEntry> logQueue;

    public LogBuffer(int bufferSize) {
        logQueue = new ArrayBlockingQueue<>(bufferSize);
    }

    public void put(LogEntry log) throws InterruptedException {
        logQueue.put(log);
    }

    public void take() throws InterruptedException {
        logQueue.take();
    }
}
