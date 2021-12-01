package ir.sharif.gamein2021.ClientHandler.controller.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.util.JWTUtil;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import ir.sharif.gamein2021.core.view.RequestObject;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProcessedRequest {

    public final RequestTypeConstant requestType;
    public WebSocketSession session;
    public String requestData;
    public Integer playerId;
    public Integer teamId;
    private Gson gson = new Gson();

    public ProcessedRequest(WebSocketSession session, String requestData, SocketSessionManager socketSessionManager) {

        this.session = session;
        this.requestData = requestData;

        RequestObject requestObject = gson.fromJson(requestData, RequestObject.class);
        JSONObject obj = new JSONObject(requestData);
        requestType = RequestTypeConstant.values()[requestObject.requestTypeConstant];
        String token = requestObject.token;

        if (socketSessionManager.isAuthenticated(session.getId())) {
            playerId = Integer.valueOf(socketSessionManager.getUserIdBySessionId(session.getId()));
            teamId = Integer.valueOf(socketSessionManager.getTeamIdBySessionId(session.getId()));
            return;
        }

        if (token != null && !token.isEmpty()) {
            try {
                DecodedJWT jwt = JWTUtil.decodeToken(token);

                if (jwt.getClaim("userId").isNull() || jwt.getClaim("teamId").isNull()) {
                    return;
                }

                playerId = Integer.valueOf(jwt.getClaim("userId").asString());
                teamId = Integer.valueOf(jwt.getClaim("teamId").asString());
                socketSessionManager.addSession(teamId.toString(), playerId.toString(), session);
            } catch (JWTDecodeException exception) {
                //Invalid token
            }
        }
    }
}
