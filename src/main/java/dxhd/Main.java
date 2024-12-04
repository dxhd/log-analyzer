package dxhd;

public class Main {

    public static String FILE_PATH = "D:\\Browser\\Telegram Desktop\\access.log";
    public static double MAX_RESPONSE_TIME_IN_MS = 50.0;
    public static double AVAILABILITY_THRESHOLD = 80.0;
    public static double MIN_INTERVAL_DURATION_IN_MS = 3000;

    public static void main(String[] args) {
        try {
            LogBuffer logBuffer = new LogBuffer(10);
            LogAnalyzer logAnalyzer = new LogAnalyzer(AVAILABILITY_THRESHOLD, MAX_RESPONSE_TIME_IN_MS, MIN_INTERVAL_DURATION_IN_MS, logBuffer);
            FileLogReader logReader = new FileLogReader(logBuffer);

            new Thread(() -> logReader.readIntoBuffer(FILE_PATH)).start();

            new Thread(() -> {
                try {
                    logAnalyzer.analyzeLogs();
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
