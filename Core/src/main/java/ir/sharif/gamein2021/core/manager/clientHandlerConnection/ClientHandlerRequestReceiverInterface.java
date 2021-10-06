package ir.sharif.gamein2021.core.manager.clientHandlerConnection;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;

/**
 * Receives requests which are sent to client.
 */
public interface ClientHandlerRequestReceiverInterface {
    void receive(BaseClientHandlerRequest request) throws InterruptedException;
}
