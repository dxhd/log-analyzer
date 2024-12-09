package dxhd.exception;

/**
 * Исключение, выбрасываемое при ошибке чтения файла с логами.
 */
public class LogReadingException extends RuntimeException {
    public LogReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
