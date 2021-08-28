package ir.sharif.gamein2021.ClientHandler.transport.model;

import ir.sharif.gamein2021.core.util.RequestTypeConstant;

import java.io.Serializable;

public class RequestObject implements Serializable
{
    private RequestTypeConstant requestTypeConstant;
    private Object requestData;

    public RequestObject(RequestTypeConstant requestTypeConstant, Object requestData)
    {
        this.requestTypeConstant = requestTypeConstant;
        this.requestData = requestData;
    }
}
