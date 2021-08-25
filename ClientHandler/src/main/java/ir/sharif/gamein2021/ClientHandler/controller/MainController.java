package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.service.PushMessageService;
import ir.sharif.gamein2021.ClientHandler.service.SocketSessionService;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;

@Controller
public class MainController {
    private Gson gson;

    @Autowired
    private TeamController teamController;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private SocketSessionService socketSessionService;

    public MainController(Gson gson) {
        this.gson = gson;
    }

    public void HandleMessage(JSONObject requestDataJsonObject, RequestTypeConstant requestType, String requestData, WebSocketSession session){
        switch (requestType) {
            case LOGIN -> {
                LoginRequest request = gson.fromJson(requestData, LoginRequest.class);
//                    ResponseObject<Object> response = authenticateHandler
//                            .authenticate(request
//                                    , ChanceHandler.getInstance().getChance(session.getId()));

                String teamName = request.getTeamName();
                String password = request.getPassword();

                try {
                    Long teamId = teamController.getTeamId(teamName, password);
                    socketSessionService.addSession(teamId.toString(), "1", session);
                    LoginResponse loginResponse = new LoginResponse(teamId);
                    pushMessageService.sendMessageBySessionId(session.getId(), gson.toJson(loginResponse));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TODO : add session to list


//                if (response.type == RequestTypeConstant.AuthenticateResponse) {
//                    int playerId = ((AuthenticationResponse) response.data).getPlayerId();
//                    HashSet<String> sessionIds = playerIdToSessionId.get(playerId);
//                    if (sessionIds == null) {
//                        sessionIds = new HashSet<>();
//                    }
//                    sessionIds.add(session.getId());
//                    if (usernameToSessionId.containsKey(request.getTeamName())) {
//                        String sessionId = usernameToSessionId.get(request.getTeamName());
//                        sessionIds.remove(sessionId);
//                        if (sessions.containsKey(sessionId)) {
//                            WebSocketSession session1 = sessions.get(sessionId);
//                            session1.close();
//                        }
//                        sessions.remove(sessionId);
//                    }
//                    sessions.put(session.getId(), session);
//                    playerIdToSessionId.put(playerId, sessionIds);
//                    usernameToSessionId.put(request.getTeamName(), session.getId());
//                    System.out.println("\tAuthenticated: " + request.getTeamName());
//                    session.sendMessage(new TextMessage(gson.toJson(response)));
//                } else {
//                    session.sendMessage(new TextMessage(gson.toJson(response)));
//                    session.close();
//                }
            }


            default -> {

            }
        }
    }

}
