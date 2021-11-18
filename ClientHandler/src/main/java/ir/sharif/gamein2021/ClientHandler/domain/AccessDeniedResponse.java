package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class AccessDeniedResponse extends ResponseObject implements Serializable {
    public AccessDeniedResponse() {
        responseTypeConstant = ResponseTypeConstant.ACCESS_DENIED.ordinal();
    }
}
