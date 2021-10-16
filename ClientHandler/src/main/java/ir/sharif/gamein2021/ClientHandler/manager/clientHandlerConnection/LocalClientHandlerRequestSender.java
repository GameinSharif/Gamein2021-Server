package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;
import org.springframework.stereotype.Component;

/**
 * Can be used when you want to say this client to do something.
 */
@Component
public class LocalClientHandlerRequestSender implements ClientHandlerRequestSenderInterface {
    private final LocalClientHandlerRequestReceiver requestReceiver;

    public LocalClientHandlerRequestSender(LocalClientHandlerRequestReceiver requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    @Override
    public void send(BaseClientHandlerRequest request) {
        try {
            requestReceiver.receive(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
