package dxhd.config;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Класс конфигурации.
 */
public class Config {

    /**
     * Форматтер для даты и времени для парсера.
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss Z");

    /**
     * Регулярное выражение для парсинга строки лога.
     */
    public static final Pattern LOG_PATTERN = Pattern.compile("^(\\S+) - - \\[(.*?)\\] \"(.*?)\" (\\d+) (\\d+) (\\S+) \"(.*?)\" \"(.*?)\".*$");

    /**
     * Минимальная продолжительность интервала с низкой доступностью в миллисекундах.
     */
    public static double MIN_INTERVAL_DURATION_IN_MS = 3000;

    /**
     * Размер буфера, через который проходит поток логов.
     */
    public static int LOG_BUFFER_SIZE = 1000;

    /**
     * Путь к файлу с логами.
     */
    public static String FILE_PATH = "D:\\Browser\\Telegram Desktop\\access.log";

}
