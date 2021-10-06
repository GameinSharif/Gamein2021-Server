package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

public class ClientsSendMessageByTeamIdRequest extends BaseClientHandlerRequest {
    private String teamId;

    public ClientsSendMessageByTeamIdRequest(String message, String teamId) {
        super(message);
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }
}
