package ir.sharif.gamein2021.ClientHandler.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class SocketSessionService {
    private Map<String, WebSocketSession> sessionBySessionId = new HashMap<>();

    private Map<String, HashSet<String>> sessionIdsByTeamId = new HashMap<>();
    private Map<String, String> teamIdByUserId = new HashMap<>();

    private Map<String, String> sessionIdByUserId = new HashMap<>();
    private Map<String, String> userIdBySessionId = new HashMap<>();

    public void addSession(String teamId, String userId, WebSocketSession session) {
        synchronized (this) {
            String sessionId = session.getId();
            sessionBySessionId.put(sessionId, session);

            teamIdByUserId.put(userId, teamId);
            if (sessionIdsByTeamId.containsKey(teamId)) {
                HashSet<String> teamSessionsIds = sessionIdsByTeamId.get(teamId);
                teamSessionsIds.add(sessionId);
            } else {
                HashSet<String> teamSessionIds = new HashSet<>();
                teamSessionIds.add(sessionId);
                sessionIdsByTeamId.put(teamId, teamSessionIds);
            }

            sessionIdByUserId.put(userId, sessionId);
            userIdBySessionId.put(sessionId, userId);
        }
    }

    public WebSocketSession removeSession(String sessionId) {
        synchronized (this) {
            WebSocketSession session = sessionBySessionId.remove(sessionId);

            String userId = userIdBySessionId.remove(sessionId);
            sessionIdByUserId.remove(userId);

            String teamId = teamIdByUserId.remove(userId);
            HashSet<String> teamSessionIds = sessionIdsByTeamId.get(teamId);
            teamSessionIds.remove(sessionId);

            if (teamSessionIds.isEmpty()) {
                sessionIdsByTeamId.remove(teamId);
            }

            return session;
        }
    }

    public WebSocketSession closeSessionBySessionId(String sessionId) throws IOException {
        WebSocketSession session = sessionBySessionId.get(sessionId);
        session.close();
        return session;
    }

    public WebSocketSession closeSessionByUserId(String userId) throws IOException {
        String sessionId = sessionIdByUserId.get(userId);
        return closeSessionBySessionId(sessionId);
    }

    public List<WebSocketSession> closeSessionsByTeamId(String teamId) throws IOException {
        HashSet<String> teamSessionIds = sessionIdsByTeamId.get(teamId);

        List<WebSocketSession> sessions = new ArrayList<>();

        for (String sessionId : teamSessionIds) {
            WebSocketSession session = closeSessionBySessionId(sessionId);
            sessions.add(session);
        }

        return sessions;
    }

    public WebSocketSession getSessionBySessionId(String sessionId) {
        return sessionBySessionId.getOrDefault(sessionId, null);
    }

    public WebSocketSession getSessionByUserId(String userId) {
        String sessionId = sessionIdByUserId.getOrDefault(userId, "");
        return getSessionBySessionId(sessionId);
    }

    public List<WebSocketSession> getSessionsByTeamId(String teamId) {
        List<WebSocketSession> sessions = new ArrayList<>();

        HashSet<String> sessionIds = sessionIdsByTeamId.getOrDefault(teamId, new HashSet<>());
        for (String sessionId : sessionIds) {
            WebSocketSession session = getSessionBySessionId(sessionId);
            if(session != null){
                sessions.add(session);
            }
        }

        return sessions;
    }

    public List<WebSocketSession> getAllSessions(){
        return new ArrayList<WebSocketSession>(sessionBySessionId.values());
    }

    public String getSessionIdByUserId(String userId){
        return sessionIdByUserId.getOrDefault(userId, null);
    }

    public List<String> getSessionIdsByTeamId(String teamId){
        return new ArrayList<String>(sessionIdsByTeamId.getOrDefault(teamId, new HashSet<>()));
    }
}
