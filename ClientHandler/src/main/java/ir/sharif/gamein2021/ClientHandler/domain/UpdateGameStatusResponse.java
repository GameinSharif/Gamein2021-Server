package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class UpdateGameStatusResponse extends ResponseObject implements Serializable {
    private final GameStatus gameStatus;

    public UpdateGameStatusResponse(GameStatus gameStatus) {
        responseTypeConstant = ResponseTypeConstant.UPDATE_GAME_STATUS.ordinal();
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
