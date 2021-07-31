package ir.sharif.gamein2021.ClientHandler.transport.model;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private int type;
    private Object decData;
    private int playerId;

    public int getType() {
        return type;
    }

    public Object getDecData() {
        return decData;
    }

    public int getPlayerId() {
        return playerId;
    }
}
