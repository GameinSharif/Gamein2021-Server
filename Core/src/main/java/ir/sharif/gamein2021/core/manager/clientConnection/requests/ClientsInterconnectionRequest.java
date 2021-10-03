package ir.sharif.gamein2021.core.manager.clientConnection.requests;

import java.io.Serializable;

public class ClientsInterconnectionRequest implements Serializable {
    private String message;

    public ClientsInterconnectionRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
