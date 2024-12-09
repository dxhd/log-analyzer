package dxhd.service.impl;

import dxhd.service.LogAnalyzer;
import dxhd.utils.LogBuffer;
import dxhd.model.LogEntry;
import dxhd.model.TimeInterval;
import dxhd.utils.IntervalUtils;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Анализатор логов, который отслеживает интервалы времени с низкой доступностью (availability).
 */
@RequiredArgsConstructor
public class LogAnalyzerImpl implements LogAnalyzer {

    /**
     * Порог доступности (в процентах), ниже которого интервал считается недоступным.
     */
    private final double availabilityThreshold;

    /**
     * Максимальное время ответа (в миллисекундах), при превышении которого лог считается ошибочным.
     */
    private final double maxResponseTime;

    /**
     * Минимальная продолжительность интервала (в миллисекундах).
     */
    private final double minIntervalDuration;

    /**
     * Буфер логов, из которого анализатор получает записи логов.
     */
    private final LogBuffer logBuffer;

    /**
     * Текущий анализируемый интервал.
     */
    private TimeInterval currentInterval;

    /**
     * Анализирует логи из буфера {@link LogBuffer}.
     * Метод работает, пока из буфера не вернётся null.
     */
    public void analyzeLogs()  {
        while (true) {
            var logEntry = logBuffer.poll(10, TimeUnit.SECONDS);
            if (logEntry == null) {
                if (currentInterval != null) {
                    closeAndPrintInterval();
                }
                break;
            }
            processLogEntry(logEntry);
        }
    }

    /**
     * Обрабатывает очередную запись лога.
     *
     * @param logEntry запись лога
     */
    private void processLogEntry(LogEntry logEntry) {

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

    /**
     * Обрабатывает запись лога, которая считается ошибочной.
     *
     * @param logEntry запись лога
     */
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

    /**
     * Обрабатывает запись лога, которая считается успешной.
     *
     * @param logEntry запись лога
     */
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

    /**
     * Закрывает текущий интервал и выводит его в консоль, если его доступность ниже порога.
     * После этого текущий интервал сбрасывается.
     */
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

    /**
     * Проверяет, нужно ли завершить текущий интервал.
     * Интервал нужно завершить, если есть фиктивная дата окончания
     * и добавление следующего лога сделает текущую доступность выше или равной порогу.
     *
     * @return {@code true}, если интервал нужно завершить, {@code false} в противном случае
     */
    private boolean shouldEndInterval() {
        return currentInterval.getEnd() != null
                && IntervalUtils.calculateAvailability(currentInterval.getSuccessCount() + 1, currentInterval.getTotalCount() + 1) >= availabilityThreshold;
    }

    /**
     * Проверяет, является ли запись лога ошибочной.
     * Запись считается ошибочной, если код ответа начинается с "4" или "5" или время ответа превышает {@link #maxResponseTime}.
     *
     * @param logEntry запись лога
     * @return {@code true}, если запись лога считается ошибочной, {@code false} в противном случае
     */
    public Boolean isLogFailure(LogEntry logEntry) {
        return logEntry.getStatusCode().startsWith("4")
                || logEntry.getStatusCode().startsWith("5")
                || logEntry.getResponseTime() > maxResponseTime;
    }
}
