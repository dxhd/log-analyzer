package dxhd;

import dxhd.service.LogAnalyzer;
import dxhd.service.LogParser;
import dxhd.service.impl.FileLogReaderImpl;
import dxhd.service.impl.InputStreamLogReaderImpl;
import dxhd.service.impl.LogAnalyzerImpl;
import dxhd.service.impl.LogParserImpl;
import dxhd.service.LogReader;
import dxhd.utils.LogBuffer;

import java.io.IOException;

import static dxhd.config.Config.DATE_TIME_FORMATTER;
import static dxhd.config.Config.FILE_PATH;
import static dxhd.config.Config.LOG_BUFFER_SIZE;
import static dxhd.config.Config.LOG_PATTERN;
import static dxhd.config.Config.MIN_INTERVAL_DURATION_IN_MS;

public class Main {

    public static void main(String[] args) throws IOException {
        double maxResponseTimeInMs = -1;
        double availabilityThreshold = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                availabilityThreshold = Double.parseDouble(args[++i]);
            } else if (args[i].equals("-t")) {
                maxResponseTimeInMs = Integer.parseInt(args[++i]);
            }
        }

        if (maxResponseTimeInMs < 0 || availabilityThreshold < 0) {
            return;
        }

        LogParser logParser = new LogParserImpl(LOG_PATTERN, DATE_TIME_FORMATTER);
        LogReader logReader;
        if (System.in.available() > 0) {
            logReader = new InputStreamLogReaderImpl(logParser, System.in);
        } else if (!FILE_PATH.isBlank()) {
            logReader = new FileLogReaderImpl(logParser, FILE_PATH);
        } else {
            return;
        }

        LogBuffer logBuffer = new LogBuffer(LOG_BUFFER_SIZE);
        LogAnalyzer logAnalyzer = new LogAnalyzerImpl(availabilityThreshold, maxResponseTimeInMs, MIN_INTERVAL_DURATION_IN_MS, logBuffer);


        new Thread(() -> logReader.readIntoBuffer(logBuffer)).start();

        new Thread(() -> {
            try {
                logAnalyzer.analyzeLogs();
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }).start();
    }
}
