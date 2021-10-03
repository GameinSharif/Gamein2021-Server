package ir.sharif.gamein2021.core.manager.engineConnection;

import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;

public interface EngineQueueConsumerInterface {
    void receive(BaseEngineRequest request);
}
