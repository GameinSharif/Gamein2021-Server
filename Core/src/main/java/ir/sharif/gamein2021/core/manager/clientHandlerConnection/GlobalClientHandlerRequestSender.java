package ir.sharif.gamein2021.core.manager.clientHandlerConnection;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.BaseClientHandlerRequest;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Sends request to all ClientHandlers.
 */
@Profile(value = {"microservice"})
@Primary
@Service(value = "GlobalClientHandlerRequestSender")
public class GlobalClientHandlerRequestSender implements ClientHandlerRequestSenderInterface {
    private final RabbitTemplate template;
    private final FanoutExchange fanoutExchange;

    public GlobalClientHandlerRequestSender(RabbitTemplate template, FanoutExchange fanoutExchange) {
        this.template = template;
        this.fanoutExchange = fanoutExchange;
    }

    @Override
    public void send(BaseClientHandlerRequest request) {
        template.convertAndSend(fanoutExchange.getName(), "", request);
    }
}
