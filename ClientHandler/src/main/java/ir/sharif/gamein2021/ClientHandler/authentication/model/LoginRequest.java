package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    public String TeamName;
    public String Password;

    public String getTeamName() {
        return TeamName;
    }

    public String getPassword() {
        return Password;
    }
}
