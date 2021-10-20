package ir.sharif.gamein2021.core.manager.centralEngineConnection;

import ir.sharif.gamein2021.core.manager.centralEngineConnection.requests.BaseEngineRequest;
import org.springframework.stereotype.Component;

/**
 * Handles requests which are sent to CentralEngine
 */
@Component(value = "LocalCentralEngineRequestReceiver")
public class LocalCentralEngineRequestReceiver implements CentralEngineRequestReceiverInterface {

    public void receive(BaseEngineRequest request) {
        System.out.println("message received in CentralEngine : " + request);
        //TODO
    }
}
