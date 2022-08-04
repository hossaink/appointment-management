package ai.giskard.exercise.agendamanagement.model.functional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;

public class AvailableSlot implements Serializable, Comparable<AvailableSlot> {
    private Long receiverUserId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    private long duration;
    private boolean noConflict;

    public AvailableSlot() {
    }

    public AvailableSlot(Long receiverUserId, LocalTime startTime, LocalTime endTime, LocalDate date, boolean noConflict) {
        this.receiverUserId = receiverUserId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = MINUTES.between(startTime, endTime);
        this.date = date;
        this.noConflict = noConflict;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public boolean isNoConflict() {
        return noConflict;
    }

    public void setNoConflict(boolean noConflict) {
        this.noConflict = noConflict;
    }

    @Override
    public int compareTo(AvailableSlot o) {
        if(this.date == o.date)
            return this.startTime.compareTo(o.startTime);
        else
            return this.date.compareTo(o.date);
    }
}
