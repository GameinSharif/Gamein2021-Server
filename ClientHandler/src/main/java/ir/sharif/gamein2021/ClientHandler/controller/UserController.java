package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.core.db.Context;
import ir.sharif.gamein2021.core.entity.User;
import org.springframework.stereotype.Controller;

@Controller
public class UserController extends Context {

    public UserController() {}

    public User getUser(String username, String password) {

        User user = userDBAccessor.getByUsername(username);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
            return null;
        }
        return null;

    }
}
