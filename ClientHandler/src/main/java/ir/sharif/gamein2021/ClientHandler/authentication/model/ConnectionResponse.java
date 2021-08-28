package ir.sharif.gamein2021.ClientHandler.authentication.model;

import java.security.PublicKey;
import java.security.interfaces.RSAKey;

public class ConnectionResponse {

    private PublicKey publicKey;

    public ConnectionResponse(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
