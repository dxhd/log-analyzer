package dxhd.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntervalUtilsTest {

    @Test
    @DisplayName("Тест расчёта доступности")
    void calculateAvailability_shouldReturnRightValue() {
        assertEquals(90, IntervalUtils.calculateAvailability(9, 10));
    }

    @Test
    @DisplayName("Тест расчёта доступности при общем количестве логов равном нулю")
    void calculateAvailability_shouldThrowException_whenTotalCountIsZero() {
        assertEquals(0.0, IntervalUtils.calculateAvailability(10, 0));
    }
}