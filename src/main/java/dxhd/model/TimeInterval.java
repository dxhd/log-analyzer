package dxhd.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Временной интервал с низкой доступностью, используемый для анализа доступности.
 */
@Setter
@Getter
public class TimeInterval {

    /**
     * Дата и время начала интервала.
     */
    private LocalDateTime start;

    /**
     * Дата и время окончания интервала.
     */
    private LocalDateTime end;

    /**
     * Дата и время окончания интервала в момент последнего сбоя.
     */
    private LocalDateTime endOnLastFailure;

    /**
     * Количество успешных запросов в интервале.
     */
    private Integer successCount = 0;

    /**
     * Количество ошибочных запросов в интервале.
     */
    private Integer failureCount = 0;

    /**
     * Общее количество запросов в интервале.
     */
    private Integer totalCount = 0;

    /**
     * Доступность в интервале.
     */
    private Double availability;

    /**
     * Доступность, рассчитанная в момент последнего сбоя.
     */
    private Double availabilityOnLastFailure;


    /**
     * Увеличивает счетчик ошибочных запросов на 1.
     */
    public void incrementFailure() {
        failureCount++;
    }

    /**
     * Увеличивает счетчик успешных запросов на 1.
     */
    public void incrementSuccess() {
        successCount++;
    }

    /**
     * Увеличивает общий счетчик запросов на 1.
     */
    public void incrementTotal() {
        totalCount++;
    }

}
