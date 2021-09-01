package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoginService
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final TeamController teamController;
    private final SocketSessionService socketSessionService;
    private final PushMessageService pushMessageService;
    private final EncryptDecryptService encryptDecryptService;
    private final Gson gson = new Gson();

    public LoginService(TeamController teamController, SocketSessionService socketSessionService,
                        PushMessageService pushMessageService, EncryptDecryptService encryptDecryptService)
    {
        this.teamController = teamController;
        this.socketSessionService = socketSessionService;
        this.pushMessageService = pushMessageService;
        this.encryptDecryptService = encryptDecryptService;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest)
    {
        if (socketSessionService.isAuthenticated(request.session.getId()))
        {
            //TODO Can send message to user.
            return;
        }

        String username = loginRequest.getUsername();
        String password = encryptDecryptService.decryptMessage(loginRequest.getPassword());

        LoginResponse loginResponse;
        try
        {
            int teamId = teamController.getTeamId(username, password);
            socketSessionService.addSession(String.valueOf(teamId),"1", request.session);

            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, teamId, "Successful");
        }
        catch (Exception e)
        {
            logger.debug(e);
            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, 0, e.getMessage());
        }

        pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(loginResponse));
    }
}
