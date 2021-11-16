package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.util.models.GameStatus;

public class UpdateGameStatusRequest extends BaseClientHandlerRequest {
    private GameStatus gameStatus;

    public UpdateGameStatusRequest(String message, GameStatus gameStatus) {
        super(message);
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
