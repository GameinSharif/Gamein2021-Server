package ir.sharif.gamein2021.CentralEngine.manager;

import ir.sharif.gamein2021.core.manager.engineConnection.EngineQueueConsumerInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"microservice"})
@Primary
@Component(value = "EngineQueueConsumer")
@RabbitListener(queues = "engineQueue")
public class EngineQueueConsumer implements EngineQueueConsumerInterface {
    private final EngineQueueConsumerInterface engineRequestHandler;

    public EngineQueueConsumer(@Qualifier(value = "EngineRequestHandler") EngineQueueConsumerInterface engineRequestHandler) {
        this.engineRequestHandler = engineRequestHandler;
    }

    @RabbitHandler
    public void receive(BaseEngineRequest request) {
        engineRequestHandler.receive(request);
    }
}
