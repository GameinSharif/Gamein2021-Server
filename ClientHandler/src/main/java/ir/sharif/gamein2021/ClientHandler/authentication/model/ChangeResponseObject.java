package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.io.Serializable;

public class ChangeResponseObject implements Serializable {
    private String chance;

    public ChangeResponseObject(String chance) {
        this.chance = chance;
    }

}
