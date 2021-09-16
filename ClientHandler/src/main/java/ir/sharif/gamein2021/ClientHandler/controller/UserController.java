package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.entity.User;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class UserController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final SocketSessionManager socketSessionManager;
    private final PushMessageManager pushMessageManager;
    private final EncryptDecryptManager encryptDecryptManager;
    private final Gson gson = new Gson();

    public UserController(SocketSessionManager socketSessionManager, PushMessageManager pushMessageManager, EncryptDecryptManager encryptDecryptManager)
    {
        this.socketSessionManager = socketSessionManager;
        this.pushMessageManager = pushMessageManager;
        this.encryptDecryptManager = encryptDecryptManager;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest)
    {
        if (socketSessionManager.isAuthenticated(request.session.getId()))
        {
            //TODO Can send message to user.
            return;
        }

        String username = loginRequest.getUsername();
//        String password = encryptDecryptService.decryptMessage(loginRequest.getPassword());
        String password = loginRequest.getPassword();

        LoginResponse loginResponse;
        try
        {
            User user = null;
            //TODO
            if (user != null)
            {
                int teamId = user.getTeam().getId();
                socketSessionManager.addSession(String.valueOf(teamId), String.valueOf(user.getId()), request.session);
                loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, user.getId(), "Successful");
            }
            else
            {
                loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, -1, "Username or Password in incorrect");
            }
        } catch (Exception e)
        {
            logger.debug(e);
            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, 0, e.getMessage());
        }

        pushMessageManager.sendMessageBySessionId(request.session.getId(), gson.toJson(loginResponse));
    }
}
