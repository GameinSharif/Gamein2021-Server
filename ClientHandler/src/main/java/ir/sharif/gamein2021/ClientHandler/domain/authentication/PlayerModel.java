package ir.sharif.gamein2021.ClientHandler.domain.authentication;

import lombok.AllArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
public class PlayerModel implements Serializable {
    private int playerId;
    private String teamName;

}
