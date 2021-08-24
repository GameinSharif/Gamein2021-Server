package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private Long teamId;

    public LoginResponse(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTeamId() {
        return teamId;
    }

}
