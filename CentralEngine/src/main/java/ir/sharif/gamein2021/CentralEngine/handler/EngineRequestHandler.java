package ir.sharif.gamein2021.CentralEngine.handler;

import ir.sharif.gamein2021.CentralEngine.manager.PushMessageManagerWrapper;
import org.springframework.stereotype.Component;

@Component
public class EngineRequestHandler {
    private PushMessageManagerWrapper pushMessageManager;

    public EngineRequestHandler(PushMessageManagerWrapper pushMessageManager) {
        this.pushMessageManager = pushMessageManager;
    }

    public void handle(){
        //TODO
    }
}
