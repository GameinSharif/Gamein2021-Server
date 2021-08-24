package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;
import java.util.List;

public class LoginRequest implements Serializable {
    public String teamName;
    public String password;

    public String getTeamName() {
        return teamName;
    }

    public String getPassword() {
        return password;
    }
}
