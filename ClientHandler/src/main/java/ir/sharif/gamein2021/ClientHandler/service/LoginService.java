package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.controller.UserController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.db.Context;
import ir.sharif.gamein2021.core.entity.User;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends Context {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final UserController userController;
    private final SocketSessionService socketSessionService;
    private final PushMessageService pushMessageService;
    private final EncryptDecryptService encryptDecryptService;
    private final Gson gson = new Gson();

    public LoginService(UserController userController, SocketSessionService socketSessionService,
                        PushMessageService pushMessageService, EncryptDecryptService encryptDecryptService) {
        this.userController = userController;
        this.socketSessionService = socketSessionService;
        this.pushMessageService = pushMessageService;
        this.encryptDecryptService = encryptDecryptService;
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
                int teamId = user.getTeam().getId();
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
