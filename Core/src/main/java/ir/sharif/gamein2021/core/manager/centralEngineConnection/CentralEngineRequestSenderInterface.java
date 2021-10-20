package ir.sharif.gamein2021.core.manager.centralEngineConnection;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;

/**
 * Capable of sending request to CentralEngine requestReceiver.
 */
public interface CentralEngineRequestSenderInterface {
    void send(BaseEngineRequest request);
}
