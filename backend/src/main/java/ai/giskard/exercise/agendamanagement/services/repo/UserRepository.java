package ai.giskard.exercise.agendamanagement.services.repo;

import ai.giskard.exercise.agendamanagement.model.persistance.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    ;
}
