package ir.sharif.gamein2021.ClientHandler.domain.Login;

import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;
import java.security.interfaces.RSAPublicKey;

public class ConnectionResponse extends ResponseObject implements Serializable
{
    private byte[] exponent;
    private byte[] modules;

    public ConnectionResponse(ResponseTypeConstant responseTypeConstant, RSAPublicKey publicKey)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.exponent = publicKey.getPublicExponent().toByteArray();
        this.modules = publicKey.getModulus().toByteArray();
    }

    public ConnectionResponse(byte[] exponent, byte[] modules) {
        this.exponent = exponent;
        this.modules = modules;
    }
}
