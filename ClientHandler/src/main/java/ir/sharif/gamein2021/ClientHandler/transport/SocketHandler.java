package ir.sharif.gamein2021.ClientHandler.transport;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.ChangeResponseObject;
import ir.sharif.gamein2021.ClientHandler.authentication.model.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.authentication.util.ChanceHandler;
import ir.sharif.gamein2021.ClientHandler.controller.MainController;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.service.EncryptDecryptService;
import ir.sharif.gamein2021.ClientHandler.service.SocketSessionService;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.ClientHandler.view.View;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private Gson gson;

    //    @Autowired
//    private AuthenticateHandler authenticateHandler;
    @Autowired
    private TeamController teamController;

    @Autowired
    MainController mainController;

    @Autowired
    EncryptDecryptService encryptDecryptService;

    @Autowired
    private SocketSessionService socketSessionService;

    @Autowired
    private View view;

    public SocketHandler() {
        gson = new Gson();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String encryptedMessage = message.getPayload();
            String decryptedMessage = encryptDecryptService.decryptMessage(encryptedMessage);

            JSONObject obj = new JSONObject(decryptedMessage);
            RequestTypeConstant requestType = RequestTypeConstant.values()[obj.getInt("requestTypeConstant")];

            JSONObject requestDataJsonObject;
            String requestData = "";
            if (obj.has("requestData")) {
                requestDataJsonObject = obj.getJSONObject("requestData");
                requestData = requestDataJsonObject.toString();
            }

            switch (requestType) {

                case LOGIN -> {
                    LoginRequest request = gson.fromJson(requestData, LoginRequest.class);
//                    ResponseObject<Object> response = authenticateHandler
//                            .authenticate(request
//                                    , ChanceHandler.getInstance().getChance(session.getId()));

                    String teamName = request.getTeamName();
                    String password = request.getPassword();

                    Long teamId = teamController.getTeamId(teamName, password);

                    // TODO : add session to list

                    LoginResponse loginResponse = new LoginResponse(teamId);

                    if (response.type == RequestTypeConstant.AuthenticateResponse) {
                        int playerId = ((AuthenticationResponse) response.data).getPlayerId();
                        HashSet<String> sessionIds = playerIdToSessionId.get(playerId);
                        if (sessionIds == null) {
                            sessionIds = new HashSet<>();
                        }
                        sessionIds.add(session.getId());
                        if (usernameToSessionId.containsKey(request.getTeamName())) {
                            String sessionId = usernameToSessionId.get(request.getTeamName());
                            sessionIds.remove(sessionId);
                            if (sessions.containsKey(sessionId)) {
                                WebSocketSession session1 = sessions.get(sessionId);
                                session1.close();
                            }
                            sessions.remove(sessionId);
                        }
                        sessions.put(session.getId(), session);
                        playerIdToSessionId.put(playerId, sessionIds);
                        usernameToSessionId.put(request.getTeamName(), session.getId());
                        System.out.println("\tAuthenticated: " + request.getTeamName());
                        session.sendMessage(new TextMessage(gson.toJson(response)));
                    } else {
                        session.sendMessage(new TextMessage(gson.toJson(response)));
                        session.close();
                    }
                }


                default -> {

                }
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.log(Level.DEBUG, "handleTransportError");
        socketSessionService.removeSession(session.getId());
        System.out.println("session " + session.getId() + " error");
        logger.error("session " + session.getId() + " error");
        logger.error("exception socket", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        logger.log(Level.ERROR, "afterConnectionEstablished");
        ResponseObject<ChangeResponseObject> response = new ResponseObject<>(RequestTypeConstant.CHANCE_RESPONSE, new ChangeResponseObject(ChanceHandler.getInstance().generateChance(session.getId())));
        session.sendMessage(new TextMessage(gson.toJson(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.log(Level.DEBUG, "afterConnectionClosed");
        socketSessionService.removeSession(session.getId());
        System.out.println("session " + session.getId() + " closed");
        logger.error("session " + session.getId() + " error");
        super.afterConnectionClosed(session,status);
    }
}
