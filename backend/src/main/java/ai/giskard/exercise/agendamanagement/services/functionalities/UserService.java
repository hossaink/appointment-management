package ai.giskard.exercise.agendamanagement.services.functionalities;

import ai.giskard.exercise.agendamanagement.model.persistance.User;
import ai.giskard.exercise.agendamanagement.services.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final Logger logger= LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user for the selected name
     * @param name
     * @return
     */
    public User createNewUser(String name){
        logger.debug("==>createNewUser({})", name);
        User user = new User(name);
        userRepository.save(user);
        return user;
    }

    /**
     * List all users in database
     * @return
     */
    public List<User> getAllUsers(){
        logger.debug("==>getAllUsers()");
        List<User> all = userRepository.findAll();

        logger.debug("\t{} users found in DB", all.size());
        return all;
    }
}
