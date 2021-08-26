package ir.sharif.gamein2021.ClientHandler.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@Scope("singleton")
public class SocketSessionService {
    private final Map<String, WebSocketSession> sessionBySessionId = new HashMap<>();

    private final Map<String, HashSet<String>> sessionIdsByTeamId = new HashMap<>();
    private final Map<String, String> teamIdBySessionId = new HashMap<>();

    public boolean isAuthenticated(String sessionId){
        synchronized (this){
            return sessionBySessionId.containsKey(sessionId);
        }
    }

    public void addSession(String teamId, WebSocketSession session) {
        synchronized (this) {
            String sessionId = session.getId();
            sessionBySessionId.put(sessionId, session);

            teamIdBySessionId.put(sessionId, teamId);
            if (sessionIdsByTeamId.containsKey(teamId)) {
                HashSet<String> teamSessionsIds = sessionIdsByTeamId.get(teamId);
                teamSessionsIds.add(sessionId);
            } else {
                HashSet<String> teamSessionIds = new HashSet<>();
                teamSessionIds.add(sessionId);
                sessionIdsByTeamId.put(teamId, teamSessionIds);
            }
        }
    }

    public WebSocketSession removeSession(String sessionId) {
        synchronized (this) {
            WebSocketSession session = sessionBySessionId.remove(sessionId);

            String teamId = teamIdBySessionId.remove(sessionId);
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

    public List<String> getSessionIdsByTeamId(String teamId){
        return new ArrayList<String>(sessionIdsByTeamId.getOrDefault(teamId, new HashSet<>()));
    }
}
