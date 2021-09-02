package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.core.adapters.UserAdapter;
import ir.sharif.gamein2021.core.entity.User;
import ir.sharif.gamein2021.core.model.UserModel;
import ir.sharif.gamein2021.core.repository.BaseRepository;
import ir.sharif.gamein2021.core.service.BaseService;
import ir.sharif.gamein2021.core.service.exceptions.NotFoundEntityException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final BaseService<User, UserModel> userService;
    private final BaseRepository<User> userRepository;

    public UserController(BaseRepository<User> userRepository) {
        this.userRepository = userRepository;
        this.userService = new BaseService<>(userRepository, new UserAdapter());
    }

    public UserModel getUser(String username, String password)
    {
        //TODO this part has bugs
        User user = new User(username, password);
        Example<User> userExample = Example.of(user);
        try {
            return userService.findOne(userExample);
        }
        catch (NotFoundEntityException e){
            return null;
        }
    }
}
