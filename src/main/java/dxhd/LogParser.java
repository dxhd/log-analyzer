package dxhd;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser { //TODO отрефакторить

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss Z");
    public static final Pattern LOG_PATTERN = Pattern.compile("^(\\S+) - - \\[(.*?)\\] \"(.*?)\" (\\d+) (\\d+) (\\S+) \"(.*?)\" \"(.*?)\".*$");

    public static LogEntry parse(String log) {
        Matcher matcher = LOG_PATTERN.matcher(log);
        if (matcher.matches()) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(2), DATE_TIME_FORMATTER);
                String responseCode = matcher.group(4);
                double responseTime = Double.parseDouble(matcher.group(6));
                return new LogEntry(dateTime, responseCode, responseTime);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка парсинга числового значения: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Некорректный формат строки лога: " + log);
        }
    }
}
