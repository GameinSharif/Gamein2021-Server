package ir.sharif.gamein2021.core.adapters;

import ir.sharif.gamein2021.core.entity.User;
import ir.sharif.gamein2021.core.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements EntityAdapter<User, UserModel> {
    @Override
    public User convertToEntity(UserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        return user;
    }

    @Override
    public UserModel convertToModel(User user) {
        return new UserModel(user.getId(), user.getUsername());
    }
}
