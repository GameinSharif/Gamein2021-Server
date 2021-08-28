package ir.sharif.gamein2021.ClientHandler.view;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;

public class ResponseObject implements Serializable
{
    public int responseTypeConstant;
    public Object responseData;
    public String result;

    public ResponseObject(ResponseTypeConstant responseTypeConstant, Object responseData, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.responseData = responseData;
        this.result = result;
    }
}
