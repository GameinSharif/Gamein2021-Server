package ir.sharif.gamein2021.ClientHandler.authentication.model;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;

import java.io.Serializable;

public class LoginRequest extends RequestObject implements Serializable
{
    public String username;
    public String password;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
