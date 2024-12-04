package dxhd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLogReader {

    private final LogBuffer logBuffer;

    public FileLogReader(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }

    public void readIntoBuffer(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .map(LogParser::parse)
                    .forEach(logBuffer::put);
        } catch (IOException e) {
            throw new RuntimeException(); // TODO обработать исключение
        }
    }
}