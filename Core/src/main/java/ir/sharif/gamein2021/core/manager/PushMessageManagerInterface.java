package ir.sharif.gamein2021.core.manager;

public interface PushMessageManagerInterface {
    void sendMessageBySessionId(String sessionId, String message);
    void sendMessageByTeamId(String teamId, String message);
    void sendMessageToAll(String message);
}
