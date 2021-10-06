package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.GlobalClientHandlerRequestSender;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageByTeamIdRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageByUserIdRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageToAllRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * used for saying to all ClientHandler to send message to a player or a team or all players.
 */
@Profile(value = {"microservice"})
@Primary
@Service(value = "GlobalPushMessageManager")
public class GlobalPushMessageManager implements PushMessageManagerInterface {
    private final GlobalClientHandlerRequestSender requestSender;

    public GlobalPushMessageManager(GlobalClientHandlerRequestSender requestSender) {
        this.requestSender = requestSender;
    }

    @Override
    public void sendMessageByUserId(String userId, String message) {
        ClientsSendMessageByUserIdRequest request = new ClientsSendMessageByUserIdRequest(message, userId);
        requestSender.send(request);
    }

    @Override
    public void sendMessageByTeamId(String teamId, String message) {
        ClientsSendMessageByTeamIdRequest request = new ClientsSendMessageByTeamIdRequest(message, teamId);
        requestSender.send(request);
    }

    @Override
    public void sendMessageToAll(String message) {
        ClientsSendMessageToAllRequest request = new ClientsSendMessageToAllRequest(message);
        requestSender.send(request);
    }
}
