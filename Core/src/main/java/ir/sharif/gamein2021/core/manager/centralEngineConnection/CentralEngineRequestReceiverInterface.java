package ir.sharif.gamein2021.core.manager.centralEngineConnection;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;


/**
 * Receives request in CentralEngine
 */
public interface CentralEngineRequestReceiverInterface {
    void receive(BaseEngineRequest request);
}
