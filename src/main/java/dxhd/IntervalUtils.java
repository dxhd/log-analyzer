package dxhd;

public class IntervalUtils {
    public static double calculateAvailability(int successCount, int totalCount) {
        return ((double) successCount / totalCount) * 100;
    }
    public static void printInterval(TimeInterval currentInterval) {
        System.out.println(currentInterval.getStart() + " " + currentInterval.getEnd() + " " + currentInterval.getAvailability());
    }
}
