package ir.sharif.gamein2021.ClientHandler.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.AlreadyLoginedResponse;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.JWTUtil;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.response.BanResponse;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final SocketSessionManager socketSessionManager;
    private final LocalPushMessageManager localPushMessageManager;
    private final EncryptDecryptManager encryptDecryptManager;
    private final UserService userService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public UserController(SocketSessionManager socketSessionManager, LocalPushMessageManager localPushMessageManager,
                          EncryptDecryptManager encryptDecryptManager, UserService userService, TeamService teamService,
                          GameCalendar gameCalendar) {
        this.socketSessionManager = socketSessionManager;
        this.localPushMessageManager = localPushMessageManager;
        this.encryptDecryptManager = encryptDecryptManager;
        this.userService = userService;
        this.teamService = teamService;
        this.gameCalendar = gameCalendar;
    }

    public void authenticate(ProcessedRequest request, LoginRequest loginRequest) {
        if (request.playerId != null && socketSessionManager.isAuthenticatedUser(request.playerId.toString())) {
            AlreadyLoginedResponse response = new AlreadyLoginedResponse();
            localPushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
            return;
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //password = encryptDecryptService.decryptMessage(password);

        LoginResponse loginResponse;
        try {
            UserDto userDto = userService.read(username, password);

            int teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId); //TODO not send everything maybe?

            if(teamDto.getBanEnd() != null && teamDto.getBanEnd().isAfter(gameCalendar.getCurrentDate())){
                BanResponse banResponse = new BanResponse(ResponseTypeConstant.BAN, teamDto.getBanEnd());
                localPushMessageManager.sendMessageBySession(request.session, gson.toJson(banResponse));
                return;
            }

            Map<String, String> payload = new HashMap<>();
            payload.put("userId", userDto.getId().toString());
            payload.put("teamId", teamDto.getId().toString());

            String token = JWTUtil.generateToken(payload);
            socketSessionManager.addSession(String.valueOf(teamId), String.valueOf(userDto.getId()), request.session);
            loginResponse = new LoginResponse(userDto.getId(), "Successful", teamDto, token);

        } catch (Exception e) {
            logger.debug(e);
            loginResponse = new LoginResponse(-1, "Username or Password in incorrect", null, null);
        }

        localPushMessageManager.sendMessageBySession(request.session, gson.toJson(loginResponse));
    }
}
