package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestReceiverInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Receives requests which are sent by other clients or CentralEngine and uses LocalClientHandlerRequestReceiver to handle requests.
 */
@Profile(value = {"microservice"})
@Primary
@Component
@RabbitListener(queues = "#{receiverQueue.name}")
public class GlobalClientHandlerRequestReceiver implements ClientHandlerRequestReceiverInterface {
    @Autowired
    private LocalClientHandlerRequestReceiver clientHandlerRequestReceiver;

    @RabbitHandler
    public void receive(BaseClientHandlerRequest request) throws InterruptedException {
        clientHandlerRequestReceiver.receive(request);
    }
}
