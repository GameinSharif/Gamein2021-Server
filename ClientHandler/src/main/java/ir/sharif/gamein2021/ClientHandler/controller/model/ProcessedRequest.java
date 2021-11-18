package ir.sharif.gamein2021.ClientHandler.controller.model;

import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import org.springframework.web.socket.WebSocketSession;

public class ProcessedRequest {

    public WebSocketSession session;
    public String requestData;
    public Integer playerId;
    public Integer teamId;

    public ProcessedRequest(WebSocketSession session, String requestData, SocketSessionManager socketSessionManager) {

        this.session = session;
        this.requestData = requestData;

        if (socketSessionManager.isAuthenticated(session.getId())) {
            playerId = Integer.valueOf(socketSessionManager.getUserIdBySessionId(session.getId()));
            teamId = Integer.valueOf(socketSessionManager.getTeamIdBySessionId(session.getId()));
        }
    }
}
