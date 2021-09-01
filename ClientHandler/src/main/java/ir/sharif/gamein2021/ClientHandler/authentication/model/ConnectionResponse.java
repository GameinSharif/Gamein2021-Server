package ir.sharif.gamein2021.ClientHandler.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.json.JSONPropertyIgnore;

import java.io.Serializable;
import java.security.PublicKey;

public class ConnectionResponse extends ResponseObject implements Serializable
{
    private byte[] encodedPublicKey;

    public ConnectionResponse(ResponseTypeConstant responseTypeConstant, PublicKey publicKey)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.encodedPublicKey = publicKey.getEncoded();
    }

    public ConnectionResponse(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }
}
