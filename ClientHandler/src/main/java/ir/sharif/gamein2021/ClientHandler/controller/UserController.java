package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.core.entity.User;
import ir.sharif.gamein2021.core.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username, String password) {
        //TODO this part has bugs
        User user = new User(username, password);
        Example<User> userExample = Example.of(user);
        Optional<User> optionalUser = userRepository.findOne(userExample);
        return optionalUser.orElse(null);
    }
}
