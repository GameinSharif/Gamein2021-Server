package ir.sharif.gamein2021.core.manager.clientConnection;

public interface PushMessageManagerInterface {
    void sendMessageByUserId(String userId, String message);
    void sendMessageByTeamId(String teamId, String message);
    void sendMessageToAll(String message);
}
