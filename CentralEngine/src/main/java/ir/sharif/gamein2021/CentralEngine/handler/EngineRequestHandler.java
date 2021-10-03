package ir.sharif.gamein2021.CentralEngine.handler;

import ir.sharif.gamein2021.core.manager.engineConnection.EngineQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.stereotype.Component;

@Component(value = "EngineRequestHandler")
public class EngineRequestHandler implements EngineQueueConsumerInterface {

    public void receive(BaseEngineRequest request) {
        //TODO
    }
}
