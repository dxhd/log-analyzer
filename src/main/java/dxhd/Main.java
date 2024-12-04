package dxhd;

public class Main {

    public static String FILE_PATH = "D:\\Browser\\Telegram Desktop\\access.log";
    public static double MAX_RESPONSE_TIME = 50.0;

    public static void main(String[] args) {
        LogAnalyzer logAnalyzer = new LogAnalyzer(80.0, 50L);
        FileLogReader logReader = new FileLogReader(logAnalyzer);
        logReader.readAndAnalyze(FILE_PATH);
//        var logStream = logReader.read(FILE_PATH);
//        logAnalyzer.analyzeLogs(logStream);
    }
}
