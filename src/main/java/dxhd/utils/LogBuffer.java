package dxhd.utils;

import dxhd.model.LogEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Буфер для хранения записей логов.
 * Использует {@link BlockingQueue} для реализации потокобезопасного хранения.
 */
@Slf4j
public class LogBuffer {

    /**
     * Очередь для хранения записей логов.
     */
    private final BlockingQueue<LogEntry> logQueue;

    /**
     * Создает новый буфер логов с заданным размером.
     *
     * @param bufferSize размер буфера (максимальное количество записей, которое может храниться в буфере)
     */
    public LogBuffer(int bufferSize) {
        logQueue = new ArrayBlockingQueue<>(bufferSize);
    }

    /**
     * Добавляет запись лога в буфер.
     * Если буфер заполнен, поток будет заблокирован до тех пор, пока не освободится место.
     *
     * @param logEntry запись лога
     */
    public void put(LogEntry logEntry) {
        try {
            logQueue.put(logEntry);
        } catch (InterruptedException e) {
            log.error("Другой поток помешал работе с очередью. {}\n", e.getMessage(), e);
        }
    }

    /**
     * Извлекает запись лога из буфера.
     * Если буфер пуст, поток будет заблокирован до тех пор, пока не появится запись или не истечет время ожидания.
     *
     * @param timeout время ожидания
     * @param unit    единица измерения времени ожидания
     * @return запись лога или {@code null}, если время ожидания истекло
     */
    public LogEntry poll(long timeout, TimeUnit unit) {
        try {
            return logQueue.poll(timeout, unit);
        } catch (InterruptedException e) {
            log.error("Другой поток помешал работе с очередью. {}\n", e.getMessage(), e);
            return null;
        }
    }
}
