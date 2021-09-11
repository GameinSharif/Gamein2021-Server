package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.UserController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.entity.Team;
import ir.sharif.gamein2021.core.entity.User;
import ir.sharif.gamein2021.core.repository.TeamRepository;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.jose4j.jwk.Use;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final UserController userController;
    private final SocketSessionService socketSessionService;
    private final PushMessageService pushMessageService;
    private final EncryptDecryptService encryptDecryptService;
    private final Gson gson = new Gson();

    private final TeamRepository teamRepository;

    public LoginService(UserController userController, SocketSessionService socketSessionService,
                        PushMessageService pushMessageService, EncryptDecryptService encryptDecryptService, TeamRepository teamRepository) {
        this.userController = userController;
        this.socketSessionService = socketSessionService;
        this.pushMessageService = pushMessageService;
        this.encryptDecryptService = encryptDecryptService;
        this.teamRepository = teamRepository;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest) {
        if (socketSessionService.isAuthenticated(request.session.getId())) {
            //TODO Can send message to user.
            return;
        }

        String username = loginRequest.getUsername();
//        String password = encryptDecryptService.decryptMessage(loginRequest.getPassword());
        String password = loginRequest.getPassword();

        LoginResponse loginResponse;
        try {
            User user = userController.getUser(username, password);
            if (user != null) {
                int teamId = user.getTeamId();
                socketSessionService.addSession(String.valueOf(teamId), String.valueOf(user.getId()), request.session);
                loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, user.getId(), "Successful");
            }
            else{
                loginResponse= new LoginResponse(ResponseTypeConstant.LOGIN, -1, "Username or Password in incorrect");
            }
        } catch (Exception e) {
            logger.debug(e);
            loginResponse = new LoginResponse(ResponseTypeConstant.LOGIN, 0, e.getMessage());
        }

        pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(loginResponse));
    }
}
