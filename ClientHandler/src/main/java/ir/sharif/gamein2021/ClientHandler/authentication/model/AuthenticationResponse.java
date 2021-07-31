package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
    private int playerId;

    public AuthenticationResponse(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

}
