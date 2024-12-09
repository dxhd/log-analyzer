package dxhd.service.impl;

import dxhd.model.LogEntry;
import dxhd.service.LogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class LogParserImplTest {

    private LogParser logParser;
    private DateTimeFormatter dateTimeFormatter;
    private Pattern logPattern;

    @BeforeEach
    public void setUp() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss Z");
        logPattern = Pattern.compile("^(\\S+) - - \\[(.*?)\\] \"(.*?)\" (\\d+) (\\d+) (\\S+) \"(.*?)\" \"(.*?)\".*$");
        logParser = new LogParserImpl(logPattern, dateTimeFormatter);
    }

    @Test
    @DisplayName("Тест парсинга валидного лога")
    public void parse_shouldParseLogEntry_whenValidLog() {
        String log = "192.168.32.181 - - [14/06/2024:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0";
        LogEntry expected = new LogEntry(LocalDateTime.parse("2024-06-14T16:47:02"), "200", 44.510983);

        LogEntry actual = logParser.parse(log);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест парсинга лога с неправильным форматированием даты и времени")
    public void parse_shouldThrowException_whenInvalidDateFormat() {
        String log = "192.168.32.181 - - [invalid_date] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0";

        assertNull(logParser.parse(log));
    }

    @Test
    @DisplayName("Тест парсинга лога с неправильным форматированием времени ответа")
    public void parse_shouldThrowException_whenInvalidResponseTime() {
        String log = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 invalid_time \"-\" \"@list-item-updater\" prio:0";

        assertNull(logParser.parse(log));
    }

    @Test
    @DisplayName("Тест парсинга лога с неправильным форматированием")
    public void parse_shouldReturnNull_whenInvalidLogFormat() {
        String log = "invalid_log_format";

        assertNull(logParser.parse(log));
    }
}