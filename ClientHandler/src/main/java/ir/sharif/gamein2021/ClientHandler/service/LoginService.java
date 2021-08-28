package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

        String teamName = loginRequest.getTeamName();
        String password = encryptDecryptService.decryptMessage(loginRequest.getPassword());
        System.out.println(password);
        try
        {
            Long teamId = teamController.getTeamId(teamName, password);
            socketSessionService.addSession(teamId.toString(), request.session);

            LoginResponse loginResponse = new LoginResponse(teamId);
            ResponseObject responseObject = new ResponseObject(ResponseTypeConstant.LOGIN, loginResponse, "Successful");
            pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(responseObject));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            logger.debug(e);
            ResponseObject responseObject = new ResponseObject(ResponseTypeConstant.LOGIN, null, e.getMessage());
            try
            {
                pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(responseObject));
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }
}
