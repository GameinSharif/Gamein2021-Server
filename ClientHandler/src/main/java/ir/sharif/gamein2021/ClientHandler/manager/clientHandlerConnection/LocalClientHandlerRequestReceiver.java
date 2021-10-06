package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestReceiverInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageByTeamIdRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageByUserIdRequest;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ClientsSendMessageToAllRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles ClientHandlerRequests.
 */
@Component
public class LocalClientHandlerRequestReceiver implements ClientHandlerRequestReceiverInterface {
    @Autowired
    private LocalPushMessageManager pushMessageManager;

    @Override
    public void receive(BaseClientHandlerRequest request) throws InterruptedException {
        System.out.println("message received in client : " + request.getMessage());
        if (request instanceof ClientsSendMessageToAllRequest) {
            pushMessageManager.sendMessageToAll(request.getMessage());
        } else if (request instanceof ClientsSendMessageByTeamIdRequest) {
            String teamId = ((ClientsSendMessageByTeamIdRequest) request).getTeamId();
            pushMessageManager.sendMessageByTeamId(teamId, request.getMessage());
        } else if (request instanceof ClientsSendMessageByUserIdRequest) {
            String userId = ((ClientsSendMessageByUserIdRequest) request).getUserId();
            pushMessageManager.sendMessageByUserId(userId, request.getMessage());
        } else {
            throw new RuntimeException();
        }
    }
}
