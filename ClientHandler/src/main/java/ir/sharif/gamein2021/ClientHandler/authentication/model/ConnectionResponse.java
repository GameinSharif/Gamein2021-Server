package ir.sharif.gamein2021.ClientHandler.authentication.model;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;
import java.security.PublicKey;

public class ConnectionResponse extends ResponseObject implements Serializable
{
    private PublicKey publicKey;

    public ConnectionResponse(ResponseTypeConstant responseTypeConstant, PublicKey publicKey)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.publicKey = publicKey;
    }
}
