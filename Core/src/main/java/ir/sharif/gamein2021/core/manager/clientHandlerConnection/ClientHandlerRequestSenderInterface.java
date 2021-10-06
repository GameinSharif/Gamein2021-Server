package ir.sharif.gamein2021.core.manager.clientHandlerConnection;


import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;

/**
 * Capable of sending requests to ClientHandler
 */
public interface ClientHandlerRequestSenderInterface {
    void send(BaseClientHandlerRequest request);
}
