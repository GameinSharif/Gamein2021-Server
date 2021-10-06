package ir.sharif.gamein2021.ClientHandler.manager.centralEngineConnection;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.CentralEngineRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.centralEngineConnection.CentralEngineRequestReceiverInterface;
import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalCentralEngineRequestSender implements CentralEngineRequestSenderInterface {
    @Autowired
    private CentralEngineRequestReceiverInterface engineQueueConsumer;

    @Override
    public void send(BaseEngineRequest request) {
        engineQueueConsumer.receive(request);
    }
}
