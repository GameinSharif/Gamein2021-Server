package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import java.io.Serializable;

public class BaseClientHandlerRequest implements Serializable {
    private String message;

    public BaseClientHandlerRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
