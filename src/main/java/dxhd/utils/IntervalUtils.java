package dxhd.utils;

import dxhd.model.TimeInterval;
import lombok.extern.slf4j.Slf4j;

/**
 * Утилитарный класс для работы с временными интервалами.
 */
@Slf4j
public class IntervalUtils {

    /**
     * Рассчитывает доступность (availability) на основе количества успешных логов и общего количества логов.
     *
     * @param successCount количество успешных логов
     * @param totalCount   общее количество логов
     * @return доступность в процентах (от 0.0 до 100.0) или 0.0, если totalCount равен 0.
     */
    public static double calculateAvailability(int successCount, int totalCount) {
        if (totalCount == 0) {
            log.warn("Невозможно рассчитать доступность при общем количестве логов равном нулю.");
            return 0.0;       }
        return ((double) successCount / totalCount) * 100;
    }

    /**
     * Выводит информацию о временном интервале в консоль.
     *
     * @param currentInterval временной интервал для вывода
     */
    public static void printInterval(TimeInterval currentInterval) {
        System.out.println(currentInterval.getStart() + " " + currentInterval.getEnd() + " " + currentInterval.getAvailability());
    }
}
