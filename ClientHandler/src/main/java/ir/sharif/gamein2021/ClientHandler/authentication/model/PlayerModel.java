package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;

public class PlayerModel implements Serializable {
    private int playerId;
    private String teamName;

    public int getPlayerId() {
        return playerId;
    }

    public String getTeamName() {
        return teamName;
    }
}
