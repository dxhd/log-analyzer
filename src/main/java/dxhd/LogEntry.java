package dxhd;

import java.time.LocalDateTime;

public class LogEntry {

    private final LocalDateTime dateTime;
    private final String statusCode;
    private final Double responseTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public LogEntry(LocalDateTime dateTime, String statusCode, Double responseTime) {
        this.dateTime = dateTime;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }
}
