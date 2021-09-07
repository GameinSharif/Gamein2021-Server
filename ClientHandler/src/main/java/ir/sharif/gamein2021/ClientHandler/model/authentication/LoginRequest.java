package ir.sharif.gamein2021.ClientHandler.model.authentication;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;

import java.io.Serializable;

public class LoginRequest extends RequestObject implements Serializable
{
    private String username;
    private String password;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
