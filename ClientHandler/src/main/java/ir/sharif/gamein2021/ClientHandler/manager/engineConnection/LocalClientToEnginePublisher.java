package ir.sharif.gamein2021.ClientHandler.manager.engineConnection;

import ir.sharif.gamein2021.core.manager.engineConnection.ClientToEnginePublisherInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.EngineQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalClientToEnginePublisher  implements ClientToEnginePublisherInterface {
    @Autowired
    private EngineQueueConsumerInterface engineQueueConsumer;

    @Override
    public void send(BaseEngineRequest request) {
        engineQueueConsumer.receive(request);
    }
}
