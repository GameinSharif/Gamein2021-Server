package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.models.GameStatus;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ChangeGameStatusResponse extends ResponseObject implements Serializable {
    private final GameStatus gameStatus;

    public ChangeGameStatusResponse(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
