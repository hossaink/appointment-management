package ai.giskard.exercise.agendamanagement.services.repo;

import ai.giskard.exercise.agendamanagement.model.persistance.Appointment;
import ai.giskard.exercise.agendamanagement.model.persistance.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findAppointmentsByReceiverUserAndDateAndStartTimeIsBetweenOrderByStartTime(User user, LocalDate date, LocalTime periodStart, LocalTime periodEnd);
    List<Appointment> findAppointmentsByTakerUserAndDateAndStartTimeIsBetweenOrderByStartTime(User user, LocalDate date, LocalTime periodStart, LocalTime periodEnd);

    @Query("select distinct app from #{#entityName} app where " +
            "app.date =:date and " +
            "(app.takerUser =:user or " +
            "app.receiverUser =:user)")
    List<Appointment> findAppointmentsByDateAndUser(@Param("date") LocalDate date, @Param("user") User user);
}
