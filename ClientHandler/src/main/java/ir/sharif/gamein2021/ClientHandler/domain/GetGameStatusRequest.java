package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import ir.sharif.gamein2021.core.view.RequestObject;

import java.io.Serializable;

public class GetGameStatusRequest extends RequestObject implements Serializable {
    public GetGameStatusRequest() {
        requestTypeConstant = RequestTypeConstant.GET_GAME_STATUS.ordinal();
    }
}
