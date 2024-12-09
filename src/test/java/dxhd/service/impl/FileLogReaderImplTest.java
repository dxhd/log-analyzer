package dxhd.service.impl;

import dxhd.utils.LogBuffer;
import dxhd.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileLogReaderImplTest {

    @Mock
    private LogParserImpl logParser;

    @Mock
    private LogBuffer logBuffer;

    private String filePath;

    @BeforeEach
    void setUp() {
        filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("mock-access.log")).getPath();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Тест чтения логов из файла")
    void readIntoBuffer_shouldPutThousandLogs_whenThousandLogsInFile() {
        FileLogReaderImpl fileLogReader = new FileLogReaderImpl(logParser, filePath);
        when(logParser.parse(any())).thenReturn(
                new LogEntry(LocalDateTime.now(), "200", 0.5)
        );

        fileLogReader.readIntoBuffer(logBuffer);

        verify(logBuffer, times(1000)).put(any());
    }
}