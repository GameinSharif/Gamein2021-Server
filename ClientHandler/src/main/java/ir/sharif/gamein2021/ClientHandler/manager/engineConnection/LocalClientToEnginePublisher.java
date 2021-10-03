package ir.sharif.gamein2021.ClientHandler.manager.engineConnection;

import ir.sharif.gamein2021.core.manager.engineConnection.ClientToEnginePublisherInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.stereotype.Component;

@Component
public class LocalClientToEnginePublisher  implements ClientToEnginePublisherInterface {
    @Override
    public void send(BaseEngineRequest request) {

    }
}
