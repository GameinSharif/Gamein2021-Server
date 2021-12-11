package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class AlreadyLoginedResponse extends ResponseObject implements Serializable {
    public AlreadyLoginedResponse() {
        responseTypeConstant = ResponseTypeConstant.ALREADY_LOGINED_RESPONSE.ordinal();
    }
}
