package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServerTimeResponse extends ResponseObject implements Serializable
{
    private LocalDateTime serverTime;

    public ServerTimeResponse(ResponseTypeConstant responseTypeConstant, LocalDateTime serverTime)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.serverTime = serverTime;
    }
}
