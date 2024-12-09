package dxhd.service.impl;

import dxhd.exception.LogReadingException;
import dxhd.service.LogParser;
import dxhd.service.LogReader;
import dxhd.utils.LogBuffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Класс для чтения логов из источника {@link InputStream} и записи их в буфер.
 */
@Slf4j
@RequiredArgsConstructor
public class InputStreamLogReaderImpl implements LogReader {

    /**
     * Парсер логов, используемый для преобразования строк логов в объекты {@link dxhd.model.LogEntry}.
     */
    private final LogParser logParser;

    /**
     * Источник логов.
     */
    private final InputStream inputStream;

    /**
     * Читает логи из источника и записывает их в буфер.
     *
     * @param logBuffer буфер, в который записываются логи.
     * @throws LogReadingException если происходит ошибка чтения файла
     */
    public void readIntoBuffer(LogBuffer logBuffer) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines()
                    .map(logParser::parse)
                    .filter(Objects::nonNull)
                    .forEach(logBuffer::put);
        } catch (IOException e) {
            log.error("Ошибка чтения логов из источника: {}.",inputStream, e);
            throw new LogReadingException(e.getMessage(), e);
        }
    }
}
