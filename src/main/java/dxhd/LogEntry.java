package dxhd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogEntry {

    private final LocalDateTime dateTime;
    private final String statusCode;
    private final Double responseTime;

    public Boolean isFailure() {
        return statusCode.startsWith("4")
                || statusCode.startsWith("5")
                || responseTime > Main.MAX_RESPONSE_TIME;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "dateTime=" + dateTime +
                ", statusCode='" + statusCode + '\'' +
                ", responseTime=" + responseTime +
                '}';
    }
}
