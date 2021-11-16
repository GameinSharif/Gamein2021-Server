package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.util.models.GameStatus;

public class ChangeGameStatusRequest extends BaseClientHandlerRequest {
    private GameStatus gameStatus;

    public ChangeGameStatusRequest(String message, GameStatus gameStatus) {
        super(message);
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
