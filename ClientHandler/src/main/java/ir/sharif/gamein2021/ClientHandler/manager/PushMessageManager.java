package ir.sharif.gamein2021.ClientHandler.manager;

import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class PushMessageManager implements PushMessageManagerInterface
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final SocketSessionManager socketSessionManager;
    private final EncryptDecryptManager encryptDecryptManager;

    public PushMessageManager(SocketSessionManager socketSessionManager, EncryptDecryptManager encryptDecryptManager) {
        this.socketSessionManager = socketSessionManager;
        this.encryptDecryptManager = encryptDecryptManager;
    }

    public void sendMessageBySessionId(String sessionId, String message){
        WebSocketSession session = socketSessionManager.getSessionBySessionId(sessionId);
        sendMessage(session, message);
    }

    public void sendMessageBySession(WebSocketSession session, String message){
        sendMessage(session, message);
    }

    public void sendMessageByTeamId(String teamId, String message) {
        List<WebSocketSession> sessions = socketSessionManager.getSessionsByTeamId(teamId);
        sendMessage(sessions, message);
    }

    public void sendMessageToAll(String message) {
        List<WebSocketSession> sessions = socketSessionManager.getAllSessions();
        sendMessage(sessions, message);
    }

    private void sendMessage(WebSocketSession session, String message){
        if (session == null || !session.isOpen()) {
            return;
        }
        try
        {
            //String encryptedMessage = encryptDecryptService.encryptMessage(message);
            session.sendMessage(new TextMessage(message));
        }
        catch (Exception ignored){}
    }

    private void sendMessage(List<WebSocketSession> sessions, String message) {
        if (sessions == null) {
            return;
        }

        //String encryptedMessage = encryptDecryptService.encryptMessage(message);

        for (WebSocketSession session : sessions) {
            if (session == null || !session.isOpen()) {
                return;
            }

            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                logger.debug(e);
            }
        }
    }
}
