package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final TeamController teamController;
    private final SocketSessionService socketSessionService;
    private final PushMessageService pushMessageService;
    private final Gson gson = new Gson();

    public LoginService(TeamController teamController, SocketSessionService socketSessionService, PushMessageService pushMessageService) {
        this.teamController = teamController;
        this.socketSessionService = socketSessionService;
        this.pushMessageService = pushMessageService;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest) {
        if(socketSessionService.isAuthenticated(request.session.getId())){
            //TODO Can send message to user.
            return;
        }

        String teamName = loginRequest.getTeamName();
        String password = loginRequest.getPassword();

        try {
            Long teamId = teamController.getTeamId(teamName, password);
            socketSessionService.addSession(teamId.toString(), request.session);
            LoginResponse loginResponse = new LoginResponse(teamId);
            pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(loginResponse));
        } catch (Exception e) {
            logger.debug(e);
        }
    }
}
