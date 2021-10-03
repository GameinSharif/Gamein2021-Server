package ir.sharif.gamein2021.core.manager.clientConnection;

import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageByTeamIdRequest;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageByUserIdRequest;
import ir.sharif.gamein2021.core.manager.clientConnection.requests.ClientsSendMessageToAllRequest;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = {"multiClient"})
@Primary
@Service(value = "GlobalPushMessageManager")
public class GeneralPushMessageManager implements PushMessageManagerInterface {
    private final RabbitTemplate template;
    private final FanoutExchange fanoutExchange;

    public GeneralPushMessageManager(RabbitTemplate template, FanoutExchange fanoutExchange) {
        this.template = template;
        this.fanoutExchange = fanoutExchange;
    }

    @Override
    public void sendMessageByUserId(String userId, String message) {
        ClientsSendMessageByUserIdRequest request = new ClientsSendMessageByUserIdRequest(message, userId);
        template.convertAndSend(fanoutExchange.getName(), "", request);
    }

    @Override
    public void sendMessageByTeamId(String teamId, String message) {
        ClientsSendMessageByTeamIdRequest request = new ClientsSendMessageByTeamIdRequest(message, teamId);
        template.convertAndSend(fanoutExchange.getName(), "", request);
    }

    @Override
    public void sendMessageToAll(String message) {
        ClientsSendMessageToAllRequest request = new ClientsSendMessageToAllRequest(message);
        template.convertAndSend(fanoutExchange.getName(), "", request);
    }
}
