package dxhd.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Запись лога.
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LogEntry {

    /**
     * Дата и время запроса.
     */
    private final LocalDateTime dateTime;

    /**
     * Код ответа.
     */
    private final String statusCode;

    /**
     * Время ответа в миллисекундах.
     */
    private final Double responseTime;

}
