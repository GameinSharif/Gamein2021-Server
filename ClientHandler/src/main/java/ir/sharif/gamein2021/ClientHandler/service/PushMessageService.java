package ir.sharif.gamein2021.ClientHandler.service;

import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class PushMessageService {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    @Autowired
    private SocketSessionService socketSessionService;

    @Autowired
    private EncryptDecryptService encryptDecryptService;

    public void sendMessageBySessionId(String sessionId, String message) throws IOException {
        WebSocketSession session = socketSessionService.getSessionBySessionId(sessionId);
        sendMessage(session, message);
    }

    public void sendMessageByUserId(String userId, String message) {
        WebSocketSession session = socketSessionService.getSessionByUserId(userId);

        try {
            sendMessage(session, message);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("\tError on send message to user Id : " + userId);
        }
    }

    public void sendMessageByTeamId(String teamId, String message) {
        List<WebSocketSession> sessions = socketSessionService.getSessionsByTeamId(teamId);
        sendMessage(sessions, message);
    }

    public void sendMessageToAll(String message) {
        List<WebSocketSession> sessions = socketSessionService.getAllSessions();
        sendMessage(sessions, message);
    }

    private void sendMessage(WebSocketSession session, String message) throws IOException {
        if (session == null || !session.isOpen()) {
            return;
        }

        String encryptedMessage = encryptDecryptService.encryptMessage(message);
        session.sendMessage(new TextMessage(encryptedMessage));
    }

    private void sendMessage(List<WebSocketSession> sessions, String message) {
        if (sessions == null) {
            return;
        }

        String encryptedMessage = encryptDecryptService.encryptMessage(message);

        for (WebSocketSession session : sessions) {
            if (session == null || !session.isOpen()) {
                return;
            }

            try {
                session.sendMessage(new TextMessage(encryptedMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
