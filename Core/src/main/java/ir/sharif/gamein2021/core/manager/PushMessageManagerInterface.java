package ir.sharif.gamein2021.core.manager;

/**
 * Is used for sending message to clients.
 * For more emphasis, sends message to clients, not clientHandlers.
 */
public interface PushMessageManagerInterface {
    void sendMessageByUserId(String userId, String message);
    void sendMessageByTeamId(String teamId, String message);
    void sendMessageToAll(String message);
}
