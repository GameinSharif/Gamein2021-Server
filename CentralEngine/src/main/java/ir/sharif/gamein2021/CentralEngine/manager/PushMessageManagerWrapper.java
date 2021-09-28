package ir.sharif.gamein2021.CentralEngine.manager;

import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.CentralEngine.configuration.ApplicationConfiguration;
import org.springframework.stereotype.Service;

@Service
public class PushMessageManagerWrapper implements PushMessageManagerInterface {
    private ApplicationConfiguration configuration;
    private PushMessageManagerInterface pushMessageManager;

    public PushMessageManagerWrapper(ApplicationConfiguration configuration, PushMessageManagerInterface pushMessageManager) {
        this.configuration = configuration;
        if (configuration.isSingleNode) {
            this.pushMessageManager = pushMessageManager;
        } else {
            //TODO
        }
    }

    @Override
    public void sendMessageBySessionId(String sessionId, String message) {
        pushMessageManager.sendMessageBySessionId(sessionId, message);
    }

    @Override
    public void sendMessageByTeamId(String teamId, String message) {
        pushMessageManager.sendMessageByTeamId(teamId, message);
    }

    @Override
    public void sendMessageToAll(String message) {
        pushMessageManager.sendMessageToAll(message);
    }
}
