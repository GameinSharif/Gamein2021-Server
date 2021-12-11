package ir.sharif.gamein2021.ClientHandler.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@Scope("singleton")
public class SocketSessionManager {
    private final Map<String, WebSocketSession> unAuthenticatedSessions = new HashMap<>();
    private final Map<String, WebSocketSession> sessionBySessionId = new HashMap<>();

    private final Map<String, String> sessionIdByUserId = new HashMap<>();
    private final Map<String, String> userIdBySessionId = new HashMap<>();

    private final Map<String, HashSet<String>> sessionIdsByTeamId = new HashMap<>();
    private final Map<String, String> teamIdBySessionId = new HashMap<>();

    public boolean isAuthenticated(String sessionId) {
        synchronized (this) {
            return sessionBySessionId.containsKey(sessionId);
        }
    }

    public boolean isAuthenticatedUser(String userId) {
        synchronized (this) {
            return sessionIdByUserId.containsKey(userId);
        }
    }

    public void addUnAuthenticatedSession(WebSocketSession session) {
        synchronized (this) {
            unAuthenticatedSessions.put(session.getId(), session);
        }
    }

    public void addSession(String teamId, String userId, WebSocketSession session) {
        synchronized (this) {
            String sessionId = session.getId();

            unAuthenticatedSessions.remove(sessionId);
            sessionBySessionId.put(sessionId, session);

            teamIdBySessionId.put(sessionId, teamId);

            if (sessionIdsByTeamId.containsKey(teamId)) {
                HashSet<String> teamSessionIds = sessionIdsByTeamId.get(teamId);
                teamSessionIds.add(sessionId);
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
            if (unAuthenticatedSessions.containsKey(sessionId)) {
                return unAuthenticatedSessions.remove(sessionId);
            }

            WebSocketSession session = sessionBySessionId.remove(sessionId);

            String teamId = teamIdBySessionId.remove(sessionId);
            try {
                HashSet<String> teamSessionIds = sessionIdsByTeamId.get(teamId);
                teamSessionIds.remove(sessionId);

                if (teamSessionIds.isEmpty()) {
                    sessionIdsByTeamId.remove(teamId);
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

            String userId = userIdBySessionId.remove(sessionId);
            sessionIdByUserId.remove(userId);

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
            if (session != null) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public WebSocketSession getSessionByUserId(String userId) {
        String sessionId = sessionIdByUserId.get(userId);
        return getSessionBySessionId(sessionId);
    }

    public List<WebSocketSession> getAllSessions() {
        return new ArrayList<>(sessionBySessionId.values());
    }

    public List<String> getSessionIdsByTeamId(String teamId) {
        return new ArrayList<>(sessionIdsByTeamId.getOrDefault(teamId, new HashSet<>()));
    }

    public String getUserIdBySessionId(String sessionId) {
        return userIdBySessionId.getOrDefault(sessionId, null);
    }

    public String getTeamIdBySessionId(String sessionId) {
        return teamIdBySessionId.getOrDefault(sessionId, null);
    }
}
