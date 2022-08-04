package ai.giskard.exercise.agendamanagement.model.persistance;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Appointment {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taker_id", columnDefinition = "BIGINT", nullable = false)
    private User takerUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id", columnDefinition = "BIGINT", nullable = false)
    private User receiverUser;

    private LocalDate date;

    private LocalTime startTime;
    private LocalTime endTime;

    public Appointment() {
    }

    public Appointment(User takerUser, User receiverUser, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.takerUser = takerUser;
        this.receiverUser = receiverUser;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public User getTakerUser() {
        return takerUser;
    }

    public void setTakerUser(User takerUser) {
        this.takerUser = takerUser;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}
