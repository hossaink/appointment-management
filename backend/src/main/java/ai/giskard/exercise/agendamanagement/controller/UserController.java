package ai.giskard.exercise.agendamanagement.controller;

import ai.giskard.exercise.agendamanagement.model.persistance.User;
import ai.giskard.exercise.agendamanagement.services.functionalities.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="/api/user")
public class UserController {
    private  final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/add")
    public @ResponseBody User addNewUser (@RequestParam String name) {
        return userService.createNewUser(name) ;
    }

    @GetMapping(path="/all")
    public @ResponseBody List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
