package ir.sharif.gamein2021.core.manager.clientConnection;

import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsInterconnectionRequest;

public interface ClientQueueConsumerInterface {
    void receive(ClientsInterconnectionRequest input) throws InterruptedException;
}
