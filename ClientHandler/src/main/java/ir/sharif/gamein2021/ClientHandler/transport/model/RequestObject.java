package ir.sharif.gamein2021.ClientHandler.transport.model;

import ir.sharif.gamein2021.core.util.RequestTypeConstant;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private RequestTypeConstant type;
    private Object requestData;
    private String token;

    public RequestTypeConstant getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public Object getRequestData() {
        return requestData;
    }
}
