package ai.giskard.exercise.agendamanagement.model.persistance;

import ai.giskard.exercise.agendamanagement.model.persistance.enums.RepetitionType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Availability implements Comparable<Availability> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT")
    @MapsId
    private User user;


    private LocalDate startDate;
    private LocalTime startTime;

    private LocalDate endDate;
    private LocalTime endTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "SMALLINT", nullable = false )
    private RepetitionType repetitionType = RepetitionType.NOT_REPEATED;

    private int lengthInMin;


    public Availability() {
    }

    public Availability(User user, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, RepetitionType repetitionType, int lengthInMin) {
        this.user = user;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.repetitionType = repetitionType;
        this.lengthInMin = lengthInMin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getLengthInMin() {
        return lengthInMin;
    }

    public void setLengthInMin(int lengthInMin) {
        this.lengthInMin = lengthInMin;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
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

    public RepetitionType getRepetitionType() {
        return repetitionType;
    }

    public void setRepetitionType(RepetitionType repetitionType) {
        this.repetitionType = repetitionType;
    }

    @Override
    public int compareTo(Availability o) {
        if(this.startDate == o.startDate)
            return this.startTime.compareTo(o.startTime);
        else
            return this.startDate.compareTo(o.startDate);
    }
}
