package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class UserController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final SocketSessionManager socketSessionManager;
    private final PushMessageManager pushMessageManager;
    private final EncryptDecryptManager encryptDecryptManager;
    private final UserService userService;
    private final Gson gson = new Gson();

    public UserController(SocketSessionManager socketSessionManager, PushMessageManager pushMessageManager, EncryptDecryptManager encryptDecryptManager, UserService userService)
    {
        this.socketSessionManager = socketSessionManager;
        this.pushMessageManager = pushMessageManager;
        this.encryptDecryptManager = encryptDecryptManager;
        this.userService = userService;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest)
    {
        if (socketSessionManager.isAuthenticated(request.session.getId()))
        {
            //TODO Can send message to user.
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //password = encryptDecryptService.decryptMessage(password);

        LoginResponse loginResponse;
        try
        {
            UserDto userDto = userService.read(username, password);

            int teamId = userDto.getTeam().getId();
            socketSessionManager.addSession(String.valueOf(teamId), String.valueOf(userDto.getId()), request.session);
            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, userDto.getId(), "Successful");
        }
        catch (Exception e)
        {
            logger.debug(e);
            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, -1, "Username or Password in incorrect");
        }

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(loginResponse));
    }
}
