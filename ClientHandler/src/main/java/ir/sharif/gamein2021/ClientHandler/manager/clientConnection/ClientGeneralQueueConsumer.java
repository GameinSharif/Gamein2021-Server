package ir.sharif.gamein2021.ClientHandler.manager.clientConnection;

import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.manager.clientConnection.ClientQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsInterconnectionRequest;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageByTeamIdRequest;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageByUserIdRequest;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageToAllRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"microservice"})
@Primary
@Component
@RabbitListener(queues = "#{receiverQueue.name}")
public class ClientGeneralQueueConsumer implements ClientQueueConsumerInterface {
    @Autowired
    private LocalPushMessageManager pushMessageManager;

    @RabbitHandler
    public void receive(ClientsInterconnectionRequest request) throws InterruptedException {
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
