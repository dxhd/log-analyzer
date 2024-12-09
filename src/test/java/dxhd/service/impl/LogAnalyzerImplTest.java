package dxhd.service.impl;

import dxhd.utils.LogBuffer;
import dxhd.model.LogEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogAnalyzerImplTest {

    @Mock
    private LogBuffer logBufferMock;

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Test
    @DisplayName("Тест на нахождение и вывод интервалов, когда они существуют")
    void analyzeLogs_shouldDetectIntervals_whenIntervalsExist() throws InterruptedException {
        String expectedOutput = String.join(System.lineSeparator(),
                "2024-12-05T13:00 2024-12-05T13:00:05 83.33333333333334",
                "2024-12-05T13:00:10 2024-12-05T13:00:15 83.33333333333334",
                "2024-12-05T13:00:20 2024-12-05T13:00:25 83.33333333333334",
                "2024-12-05T13:00:30 2024-12-05T13:00:35 83.33333333333334",
                "2024-12-05T13:00:40 2024-12-05T13:00:45 83.33333333333334",
                "2024-12-05T13:00:50 2024-12-05T13:00:55 83.33333333333334",
                "2024-12-05T13:01 2024-12-05T13:01:05 83.33333333333334",
                "2024-12-05T13:01:10 2024-12-05T13:01:15 83.33333333333334",
                "2024-12-05T13:01:20 2024-12-05T13:01:25 83.33333333333334",
                "2024-12-05T13:01:30 2024-12-05T13:01:35 83.33333333333334"
        ) + System.lineSeparator();
        mockLogBufferWithEntries();

        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(85, 55.0, 2000, logBufferMock);
        analyzer.analyzeLogs();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    @DisplayName("Тест на нахождение и вывод интервала, когда он занимает всё время")
    void analyzeLogs_shouldDetectOneInterval_whenAllTimeInterval() throws InterruptedException {
        String expectedOutput = String.join(System.lineSeparator(),
                "2024-12-05T13:00 2024-12-05T13:01:30 89.01098901098901"
        ) + System.lineSeparator();
        mockLogBufferWithEntries();

        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(99.9, 55.0, 2000, logBufferMock);
        analyzer.analyzeLogs();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    @DisplayName("Тест анализа логов, когда нет ошибочных интервалов")
    void analyzeLogs_shouldNotDetectIntervals_whenIntervalDoesNotExist() throws InterruptedException {
        mockLogBufferWithEntries();

        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(50, 55.0, 2000, logBufferMock);
        analyzer.analyzeLogs();

        assertEquals("", outContent.toString());
    }

    @Test
    @DisplayName("Тест проверки ошибочности лога при времени ответа, превышающем минимальное")
    void isLogFailure_shouldReturnTrue_whenLogWithHighResponseTime() {
        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(85, 55.0, 2000, null);
        LogEntry logEntry = new LogEntry(LocalDateTime.parse("2024-12-05T13:00:00"), "200", 60.0);
        assertTrue(analyzer.isLogFailure(logEntry), "Лог с превышением времени ответа должен быть failure");
    }

    @Test
    @DisplayName("Тест проверки ошибочности лога при статусе ответа 4xx")
    public void testIsLogFailure_shouldReturnTrue_whenLogWith4xxStatusCode() {
        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(85, 55.0, 2000, null);
        LogEntry logEntry = new LogEntry(LocalDateTime.parse("2024-12-05T13:00:00"), "404", 30.0);
        assertTrue(analyzer.isLogFailure(logEntry), "Лог с кодом 404 должен быть failure");
    }

    @Test
    @DisplayName("Тест проверки ошибочности лога при статусе ответа 5xx")
    public void testIsLogFailure_shouldReturnTrue_whenLogWith5xxStatusCode() {
        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(85, 55.0, 2000, null);
        LogEntry logEntry = new LogEntry(LocalDateTime.parse("2024-12-05T13:00:00"), "500", 30.0);
        assertTrue(analyzer.isLogFailure(logEntry), "Лог с кодом 500 должен быть failure");
    }

    @Test
    @DisplayName("Тест проверки ошибочности при успешном логе")
    void isLogFailure_shouldReturnFalse_WhenLogIsSuccess() {
        LogAnalyzerImpl analyzer = new LogAnalyzerImpl(85, 55.0, 2000, null);
        LogEntry logEntry = new LogEntry(LocalDateTime.parse("2024-12-05T13:00:00"), "200", 50.0);
        assertFalse(analyzer.isLogFailure(logEntry), "Лог с кодом 200 и нормальным временем ответа не должен быть failure");

    }

    private void mockLogBufferWithEntries() {
        List<LogEntry> testLogs = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.parse("2024-12-05T13:00:00");
        for (int i = 0; i < 100; i++) {
            String statusCode = i % 10 == 0 ? "500" : "200"; // Каждая 10-я запись — ошибка
            double responseTime = i % 15 == 0 ? 50.0 : 30.0; // Каждая 15-я запись превышает лимит времени
            LocalDateTime dateTime = startTime.plusSeconds(i);
            testLogs.add(new LogEntry(dateTime, statusCode, responseTime));
        }
        when(logBufferMock.poll(anyLong(), eq(TimeUnit.SECONDS)))
                .thenAnswer(invocation -> {
                    if (!testLogs.isEmpty()) {
                        return testLogs.remove(0);
                    }
                    return null;
                });
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}
