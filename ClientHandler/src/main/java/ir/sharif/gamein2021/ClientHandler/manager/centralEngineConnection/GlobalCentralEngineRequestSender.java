package ir.sharif.gamein2021.ClientHandler.manager.centralEngineConnection;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.CentralEngineRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Sends request to CentralEngine queue.
 */
@Profile(value = {"microservice"})
@Primary
@Component
public class GlobalCentralEngineRequestSender implements CentralEngineRequestSenderInterface {
    private final RabbitTemplate template;
    private final Queue queue;

    public GlobalCentralEngineRequestSender(RabbitTemplate template, @Qualifier(value = "engineQueue") Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(BaseEngineRequest request) {
        this.template.convertAndSend(queue.getName(), request);
    }
}
