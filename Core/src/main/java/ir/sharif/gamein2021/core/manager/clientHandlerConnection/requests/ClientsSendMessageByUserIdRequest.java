package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

public class ClientsSendMessageByUserIdRequest extends BaseClientHandlerRequest {
    private String userId;

    public ClientsSendMessageByUserIdRequest(String message, String userId) {
        super(message);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
