package ir.sharif.gamein2021.ClientHandler.domain.Login;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import lombok.AllArgsConstructor;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;

@AllArgsConstructor
public class LoginResponse extends ResponseObject implements Serializable
{
    private long playerId;
    private String result;

    public LoginResponse(ResponseTypeConstant responseTypeConstant, int playerId, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.playerId = playerId;
        this.result = result;
    }

}
