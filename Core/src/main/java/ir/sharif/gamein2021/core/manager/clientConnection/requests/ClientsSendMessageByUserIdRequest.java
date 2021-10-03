package ir.sharif.gamein2021.core.manager.clientConnection.requests;

public class ClientsSendMessageByUserIdRequest extends ClientsInterconnectionRequest {
    private String userId;

    public ClientsSendMessageByUserIdRequest(String message, String userId) {
        super(message);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
