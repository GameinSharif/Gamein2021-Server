package ir.sharif.gamein2021.ClientHandler.transport;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.MainController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.ConnectionResponse;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final MainController mainController;
    private final SocketSessionManager socketSessionManager;
    private final EncryptDecryptManager encryptDecryptManager;
    private final LocalPushMessageManager localPushMessageManager;
    private final Gson gson;

    @Autowired
    public SocketHandler(MainController mainController,
                         SocketSessionManager socketSessionManager,
                         EncryptDecryptManager encryptDecryptManager,
                         LocalPushMessageManager localPushMessageManager) {
        this.mainController = mainController;
        this.socketSessionManager = socketSessionManager;
        this.encryptDecryptManager = encryptDecryptManager;
        this.localPushMessageManager = localPushMessageManager;
        gson = new Gson();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            //String encryptedMessage = message.getPayload();
            //String decryptedMessage = encryptDecryptService.decryptMessage(encryptedMessage);
            ProcessedRequest processedRequest = new ProcessedRequest(session, message.getPayload(), socketSessionManager);
            mainController.HandleMessage(processedRequest);
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.debug(exception);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.log(Level.DEBUG, "handleTransportError");
        System.out.println("session " + session.getId() + " error");
        socketSessionManager.removeSession(session.getId());
        logger.error("session " + session.getId() + " error");
        logger.error("exception socket", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.log(Level.ERROR, "afterConnectionEstablished");
        socketSessionManager.addUnAuthenticatedSession(session);

        ConnectionResponse connectionResponse = new ConnectionResponse(ResponseTypeConstant.CONNECTION, encryptDecryptManager.getPublicKey());
        localPushMessageManager.sendMessageBySession(session, gson.toJson(connectionResponse));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.log(Level.DEBUG, "afterConnectionClosed");
        socketSessionManager.removeSession(session.getId());
        System.out.println("session " + session.getId() + " closed");
        logger.error("session " + session.getId() + " error");
        super.afterConnectionClosed(session, status);
    }
}
