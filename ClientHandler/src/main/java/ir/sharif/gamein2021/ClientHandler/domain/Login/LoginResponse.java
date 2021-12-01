package ir.sharif.gamein2021.ClientHandler.domain.Login;

import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;

@AllArgsConstructor
public class LoginResponse extends ResponseObject implements Serializable
{
    private long playerId;
    private String result;
    private TeamDto team;
    private String token;

    public LoginResponse(int playerId, String result, TeamDto team, String token)
    {
        this.token = token;
        this.responseTypeConstant = ResponseTypeConstant.LOGIN.ordinal();
        this.playerId = playerId;
        this.result = result;
        this.team = team;
    }

}
