package ir.sharif.gamein2021.core.manager.engineConnection;

import ir.sharif.gamein2021.core.manager.engineConnection.EngineQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.stereotype.Component;

@Component(value = "EngineRequestHandler")
public class EngineRequestHandler implements EngineQueueConsumerInterface {

    public void receive(BaseEngineRequest request) {
        System.out.println("message received in CentralEngine : " + request);
        //TODO
    }
}
