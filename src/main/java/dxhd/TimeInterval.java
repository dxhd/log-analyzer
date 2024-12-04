package dxhd;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimeInterval {

    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime endOnLastFailure;
    private Integer successCount = 0;
    private Integer failureCount = 0;
    private Integer totalCount = 0;
    private Double availability;
    private Double availabilityOnLastFailure;

    public void incrementFailure() {
        failureCount++;
    }

    public void incrementSuccess() {
        successCount++;
    }

    public void incrementTotal() {
        totalCount++;
    }

    public void decrementFailure() {
        failureCount--;
    }

    public void decrementSuccess() {
        successCount--;
    }

    public void decrementTotal() {
        totalCount--;
    }

    @Override
    public String toString() {
        return "TimeInterval{" +
                "start=" + start +
                ", end=" + end +
                ", availability=" + availability +
                '}';
    }
}
