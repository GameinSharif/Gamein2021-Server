package ir.sharif.gamein2021.ClientHandler.domain.authentication;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class LoginResponse extends ResponseObject implements Serializable
{
    private long playerId;
    private String result;

}
