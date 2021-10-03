package ir.sharif.gamein2021.core.manager.clientConnection.requests;

public class ClientsSendMessageByTeamIdRequest extends ClientsInterconnectionRequest {
    private String teamId;

    public ClientsSendMessageByTeamIdRequest(String message, String teamId) {
        super(message);
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }
}
