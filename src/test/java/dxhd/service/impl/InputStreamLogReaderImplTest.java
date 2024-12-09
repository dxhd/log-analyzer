package dxhd.service.impl;

import dxhd.model.LogEntry;
import dxhd.utils.LogBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InputStreamLogReaderImplTest {
    @Mock
    private LogParserImpl logParser;

    @Mock
    private LogBuffer logBuffer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Тест чтения логов из источника InputStream")
    void readIntoBuffer_shouldPutThousandLogs_whenThousandLogsInFile() throws FileNotFoundException {
        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("mock-access.log")).getPath();
        InputStream inputStream = new FileInputStream(filePath);
        InputStreamLogReaderImpl inputStreamLogReader = new InputStreamLogReaderImpl(logParser, inputStream);
        when(logParser.parse(any())).thenReturn(
                new LogEntry(LocalDateTime.now(), "200", 0.5)
        );

        inputStreamLogReader.readIntoBuffer(logBuffer);

        verify(logBuffer, times(1000)).put(any());
    }
}