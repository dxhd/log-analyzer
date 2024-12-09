package dxhd.service.impl;

import dxhd.model.LogEntry;
import dxhd.service.LogParser;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер логов, преобразующий строки логов в объекты {@link LogEntry}.
 */
@Slf4j
public class LogParserImpl implements LogParser {

    /**
     * Регулярное выражение, используемое для разбора строки лога.
     */
    private final Pattern logPattern;

    /**
     * Форматтер даты и времени, используемый для парсинга даты и времени из строки лога.
     */
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Создает новый парсер логов.
     *
     * @param logPattern        регулярное выражение для разбора строки лога
     * @param dateTimeFormatter форматтер даты и времени
     */
    public LogParserImpl(Pattern logPattern, DateTimeFormatter dateTimeFormatter) {
        this.logPattern = logPattern;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    /**
     * Парсит строку лога и возвращает объект {@link LogEntry}.
     *
     * @param logStr строка лога
     * @return объект {@link LogEntry} или {@code null}, если строка не соответствует формату или произошла ошибка парсинга
     */
    public LogEntry parse(String logStr) {
        Matcher matcher = logPattern.matcher(logStr);
        if (matcher.matches()) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(2), dateTimeFormatter);
                String responseCode = matcher.group(4);
                double responseTime = Double.parseDouble(matcher.group(6));
                return new LogEntry(dateTime, responseCode, responseTime);
            } catch (NumberFormatException e) {
                log.warn("Ошибка парсинга числового значения: " + e.getMessage(), e);
            } catch (DateTimeParseException e) {
                log.warn("Ошибка парсинга даты и времени: " + e.getMessage(), e);
            }
        } else {
            log.warn("Некорректный формат строки лога: " + logStr);
        }
        return null;
    }
}
