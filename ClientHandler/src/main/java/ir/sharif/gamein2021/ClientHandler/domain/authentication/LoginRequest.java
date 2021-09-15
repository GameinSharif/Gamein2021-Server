package ir.sharif.gamein2021.ClientHandler.domain.authentication;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class LoginRequest extends RequestObject implements Serializable
{
    private String username;
    private String password;

}
