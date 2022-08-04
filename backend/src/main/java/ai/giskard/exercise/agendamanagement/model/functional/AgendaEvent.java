package ai.giskard.exercise.agendamanagement.model.functional;

import ai.giskard.exercise.agendamanagement.model.functional.enums.EventType;
import ai.giskard.exercise.agendamanagement.model.persistance.Appointment;
import ai.giskard.exercise.agendamanagement.model.persistance.Availability;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;

public class AgendaEvent implements Serializable, Comparable<AgendaEvent>{
    private EventType eventType;
    private String title;
    private LocalDate date;
    private LocalTime time;
    private int duration;

    public AgendaEvent() {
    }

    public AgendaEvent(Availability availability, String title){
        this.eventType = EventType.AVAILABILITY;
        this.title = title;
        this.date = availability.getStartDate();
        this.time = availability.getStartTime();
        this.duration = (int) MINUTES.between(availability.getStartTime(), availability.getEndTime());
    }

    public AgendaEvent(Appointment appointment, String title){
        this.eventType = EventType.APPOINTMENT;
        this.title = title;
        this.date = appointment.getDate();
        this.time = appointment.getStartTime();
        this.duration = (int) MINUTES.between(appointment.getStartTime(), appointment.getEndTime());
    }

    public AgendaEvent(EventType eventType, String title, LocalDate date, LocalTime time, int duration) {
        this.eventType = eventType;
        this.title = title;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(AgendaEvent o) {
        if(this.date.equals(o.date))
            return this.time.compareTo(o.time);
        return this.date.compareTo(o.date);

    }
}
