package ir.sharif.gamein2021.ClientHandler.transport.model;

import ir.sharif.gamein2021.ClientHandler.view.Request;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private RequestTypeConstant requestTypeConstant;
    private Request requestData;

    public RequestObject(RequestTypeConstant requestTypeConstant, Request requestData) {
        this.requestTypeConstant = requestTypeConstant;
        this.requestData = requestData;
    }

    public RequestTypeConstant getRequestTypeConstant() {
        return requestTypeConstant;
    }

    public void setRequestTypeConstant(RequestTypeConstant requestTypeConstant) {
        this.requestTypeConstant = requestTypeConstant;
    }

    public Request getRequestData() {
        return requestData;
    }

    public void setRequestData(Request requestData) {
        this.requestData = requestData;
    }
}
