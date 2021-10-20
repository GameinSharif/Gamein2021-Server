package ir.sharif.gamein2021.CentralEngine.manager;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.CentralEngineRequestReceiverInterface;
import ir.sharif.gamein2021.core.manager.centralEngineConnection.LocalCentralEngineRequestReceiver;
import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Receives request from queue and uses LocalCentralEngineRequestReceiver to handle requests.
 */
@Profile(value = {"microservice"})
@Primary
@Component(value = "GlobalCentralEngineRequestReceiver")
@RabbitListener(queues = "engineQueue")
public class GlobalCentralEngineRequestReceiver implements CentralEngineRequestReceiverInterface {
    private final LocalCentralEngineRequestReceiver engineRequestReceiver;

    public GlobalCentralEngineRequestReceiver(LocalCentralEngineRequestReceiver engineRequestReceiver) {
        this.engineRequestReceiver = engineRequestReceiver;
    }

    @RabbitHandler
    public void receive(BaseEngineRequest request) {
        engineRequestReceiver.receive(request);
    }
}
