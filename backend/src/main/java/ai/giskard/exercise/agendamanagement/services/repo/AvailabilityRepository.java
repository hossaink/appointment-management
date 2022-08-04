package ai.giskard.exercise.agendamanagement.services.repo;

import ai.giskard.exercise.agendamanagement.model.persistance.Availability;
import ai.giskard.exercise.agendamanagement.model.persistance.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    List<Availability> findByUserAndEndDateAfterAndStartDateBefore(User user, LocalDate startTime, LocalDate endTime);
}
