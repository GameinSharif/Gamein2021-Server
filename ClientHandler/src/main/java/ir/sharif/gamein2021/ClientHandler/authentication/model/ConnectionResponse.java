package ir.sharif.gamein2021.ClientHandler.authentication.model;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;
import java.security.interfaces.RSAPublicKey;

public class ConnectionResponse extends ResponseObject implements Serializable
{
    private byte[] encodedPublicKey;

    public ConnectionResponse(ResponseTypeConstant responseTypeConstant, RSAPublicKey publicKey)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.encodedPublicKey = publicKey.getEncoded();
    }

    public ConnectionResponse(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }
}
