package dxhd.service.impl;

import dxhd.service.LogParser;
import dxhd.service.LogReader;
import dxhd.utils.LogBuffer;
import dxhd.exception.LogReadingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Класс для чтения логов из файла и записи их в буфер.
 */
@Slf4j
@RequiredArgsConstructor
public class FileLogReaderImpl implements LogReader {

    /**
     * Парсер логов, используемый для преобразования строк логов в объекты {@link dxhd.model.LogEntry}.
     */
    private final LogParser logParser;

    /**
     * Путь к файлу с логами.
     */
    private final String filePath;

    /**
     * Читает логи из файла и записывает их в буфер.
     *
     * @param logBuffer буфер, в который записываются логи.
     * @throws LogReadingException если происходит ошибка чтения файла
     */
    public void readIntoBuffer(LogBuffer logBuffer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .map(logParser::parse)
                    .filter(Objects::nonNull)
                    .forEach(logBuffer::put);
        } catch (IOException e) {
            log.error("Ошибка чтения файла: " + filePath, e);
            throw new LogReadingException(e.getMessage(), e);
        }
    }
}