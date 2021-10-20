package ir.sharif.gamein2021.ClientHandler.domain.Login;

import ir.sharif.gamein2021.core.view.RequestObject;
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
