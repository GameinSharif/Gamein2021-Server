package ir.sharif.gamein2021.ClientHandler.manager.clientConnection;

import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.manager.clientConnection.ClientQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsInterconnectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientLocalQueueConsumer implements ClientQueueConsumerInterface {
    @Autowired
    private LocalPushMessageManager pushMessageManager;

    @Override
    public void receive(ClientsInterconnectionRequest input) throws InterruptedException {
    }
}
