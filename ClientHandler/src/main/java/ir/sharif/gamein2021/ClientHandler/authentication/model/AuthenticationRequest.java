package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;
import java.util.List;

public class AuthenticationRequest implements Serializable {
    public String username;
    public List<String> passAndSalt;

    public String getUsername() {
        return username;
    }

    public List<String> getPassAndSalt() {
        return passAndSalt;
    }
}
