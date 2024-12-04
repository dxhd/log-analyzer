package dxhd;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
public class FileLogReader implements LogReader {

    private final LogAnalyzer logAnalyzer;

    public FileLogReader(LogAnalyzer logAnalyzer) {
        this.logAnalyzer = logAnalyzer;
    }

    public Stream<LogEntry> read(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().map(LogParser::parse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readAndAnalyze(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            reader.lines()
//                    .map(LogParser::parse)
//                    .forEach(logAnalyzer::processLogEntry);
            var logs = reader.lines()
                    .map(LogParser::parse)
                    .collect(Collectors.toList());

            for (var log : logs) {
                logAnalyzer.processLogEntry(log);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}