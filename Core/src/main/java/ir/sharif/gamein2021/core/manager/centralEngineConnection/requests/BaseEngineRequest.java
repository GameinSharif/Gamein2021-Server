package ir.sharif.gamein2021.core.manager.centralEngineConnection.requests;

import java.io.Serializable;

public class BaseEngineRequest implements Serializable {
    private final String message;

    public BaseEngineRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
