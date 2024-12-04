package dxhd;

import java.time.LocalDateTime;

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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getEndOnLastFailure() {
        return endOnLastFailure;
    }

    public void setEndOnLastFailure(LocalDateTime endOnLastFailure) {
        this.endOnLastFailure = endOnLastFailure;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getAvailability() {
        return availability;
    }

    public void setAvailability(Double availability) {
        this.availability = availability;
    }

    public Double getAvailabilityOnLastFailure() {
        return availabilityOnLastFailure;
    }

    public void setAvailabilityOnLastFailure(Double availabilityOnLastFailure) {
        this.availabilityOnLastFailure = availabilityOnLastFailure;
    }
}
