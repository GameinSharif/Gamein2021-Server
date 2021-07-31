package ir.sharif.gamein2021.ClientHandler.transport;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.AuthenticationRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.AuthenticationResponse;
import ir.sharif.gamein2021.ClientHandler.authentication.model.ChangeResponseObject;
import ir.sharif.gamein2021.ClientHandler.authentication.util.AuthenticateHandler;
import ir.sharif.gamein2021.ClientHandler.authentication.util.ChanceHandler;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.util.RequestConstants;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.ClientHandler.view.View;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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
    private boolean mainThreadWorking;

    Map<String, WebSocketSession> sessions = new HashMap<>();
    Map<Integer, HashSet<String>> playerIdToSessionId = new HashMap<>();
    Map<String, String> usernameToSessionId = new HashMap<>();
    private Gson gson;

    private AuthenticateHandler authenticateHandler;
    private View view;

    public SocketHandler(AuthenticateHandler authenticateHandler, View view) {
        this.authenticateHandler = authenticateHandler;
        this.view = view;
        gson = new Gson();
        mainThreadWorking = false;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String data = message.getPayload();
            JSONObject obj = new JSONObject(data);
            int type = obj.getInt("type");
            if (!mainThreadWorking) {
                if (type == RequestConstants.AuthenticationRequest) {
                    JSONObject decDataJsonObject;
                    String decData = "";
                    if (obj.has("decData")) {
                        decDataJsonObject = obj.getJSONObject("decData");
                        decData = decDataJsonObject.toString();
                    }
                    AuthenticationRequest request = gson.fromJson(decData, AuthenticationRequest.class);
                    ResponseObject<Object> response = authenticateHandler
                            .authenticate(request
                                    , ChanceHandler.getInstance().getChance(session.getId()));
                    if (response.type == RequestConstants.AuthenticateResponse) {
                        int playerId = ((AuthenticationResponse) response.data).getPlayerId();
                        HashSet<String> sessionIds = playerIdToSessionId.get(playerId);
                        if (sessionIds == null) {
                            sessionIds = new HashSet<>();
                        }
                        sessionIds.add(session.getId());
                        if (usernameToSessionId.containsKey(request.getUsername())) {
                            String sessionId = usernameToSessionId.get(request.getUsername());
                            sessionIds.remove(sessionId);
                            if (sessions.containsKey(sessionId)) {
                                WebSocketSession session1 = sessions.get(sessionId);
                                session1.close();
                            }
                            sessions.remove(sessionId);
                        }
                        sessions.put(session.getId(), session);
                        playerIdToSessionId.put(playerId, sessionIds);
                        usernameToSessionId.put(request.getUsername(), session.getId());
                        System.out.println("\tAuthenticated: " + request.getUsername());
                        session.sendMessage(new TextMessage(gson.toJson(response)));
                    } else {
                        session.sendMessage(new TextMessage(gson.toJson(response)));
                        session.close();
                    }
                } else {
                    if (!sessions.containsKey(session.getId())) return;
                    System.out.println("received: " + data);
                    logger.info("received: " + data);
                    String response = view.processCommands(data);
                    System.out.println("res: " + response);
                    logger.info("res: " + response);
                    session.sendMessage(new TextMessage(response));
                }
            } else {
                ResponseObject<Object> mainThreadWorking = new ResponseObject<>(RequestConstants.MAIN_THREAD_WORKING);
                session.sendMessage(new TextMessage(gson.toJson(mainThreadWorking)));
            }
        } catch (Exception e) {
            logger.error("Error on handle text message", e);
            e.printStackTrace();
        }
    }

    public void sendMessage(int playerId, String message) {
        synchronized (this) {
            if (!playerIdToSessionId.containsKey(playerId)) {
                return;
            }
            HashSet<String> sessionIds = playerIdToSessionId.get(playerId);
            if (sessionIds == null || sessionIds.size() == 0) {
                return;
            }
            HashSet<String> toRemove = new HashSet<>();
            for (String sessionId : sessionIds) {
                if (sessions.containsKey(sessionId)) {
                    WebSocketSession session = sessions.get(sessionId);
                    if (session == null || !session.isOpen()) {
                        toRemove.add(sessionId);
                    } else {
                        synchronized (sessions.get(sessionId)) {
                            try {
                                session.sendMessage(new TextMessage(message));
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("\tError on send message to player Id : " + playerId);
                            }
                            logger.info("sent to user: " + playerId + " message: " + message);
                        }
                    }
                } else {
                    toRemove.add(sessionId);
                }
            }
            sessionIds.removeAll(toRemove);
            playerIdToSessionId.put(playerId, sessionIds);
        }
    }

    public void sendToAll(String message) throws IOException {
        synchronized (this) {
            TextMessage textMessage = new TextMessage(message);
            HashSet<String> sessionIds = new HashSet<>(sessions.keySet());
            for (String sessionId : sessionIds) {
                if (sessions.get(sessionId) == null || !sessions.get(sessionId).isOpen()) {
                    sessions.remove(sessionId);
                } else {
                    synchronized (sessions.get(sessionId)) {
                        sessions.get(sessionId).sendMessage(textMessage);
                    }
                }
            }
            logger.info("sent to all: " + message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessions.remove(session.getId());
        System.out.println("session " + session.getId() + " error");
        logger.error("session " + session.getId() + " error");
        logger.error("exception socket", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        ResponseObject<ChangeResponseObject> response = new ResponseObject<>(RequestConstants.CHANCE_RESPONSE, new ChangeResponseObject(ChanceHandler.getInstance().generateChance(session.getId())));
        session.sendMessage(new TextMessage(gson.toJson(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("session " + session.getId() + " closed");
        logger.error("session " + session.getId() + " error");
    }

    public boolean isMainThreadWorking() {
        return mainThreadWorking;
    }

    public void setMainThreadWorking(boolean mainThreadWorking) {
        this.mainThreadWorking = mainThreadWorking;
    }


}
