package ir.sharif.gamein2021.ClientHandler.manager.engineConnection;

import ir.sharif.gamein2021.core.manager.engineConnection.ClientToEnginePublisherInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"microservice"})
@Primary
@Component
public class GeneralClientToEnginePublisher implements ClientToEnginePublisherInterface {
    private final RabbitTemplate template;
    private final Queue queue;

    public GeneralClientToEnginePublisher(RabbitTemplate template, @Qualifier(value = "engineQueue") Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(BaseEngineRequest request) {
        this.template.convertAndSend(queue.getName(), request);
    }
}
