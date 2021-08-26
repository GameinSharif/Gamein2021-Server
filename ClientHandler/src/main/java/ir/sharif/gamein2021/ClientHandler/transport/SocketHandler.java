package ir.sharif.gamein2021.ClientHandler.transport;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.ChangeResponseObject;
import ir.sharif.gamein2021.ClientHandler.authentication.util.ChanceHandler;
import ir.sharif.gamein2021.ClientHandler.controller.MainController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.service.EncryptDecryptService;
import ir.sharif.gamein2021.ClientHandler.service.SocketSessionService;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SocketHandler extends TextWebSocketHandler {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final MainController mainController;
    private final SocketSessionService socketSessionService;
    private final EncryptDecryptService encryptDecryptService;
    private final Gson gson;

    public SocketHandler(MainController mainController, SocketSessionService socketSessionService, EncryptDecryptService encryptDecryptService) {
        this.mainController = mainController;
        this.socketSessionService = socketSessionService;
        this.encryptDecryptService = encryptDecryptService;
        gson = new Gson();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String encryptedMessage = message.getPayload();
            String decryptedMessage = encryptDecryptService.decryptMessage(encryptedMessage);

            JSONObject obj = new JSONObject(decryptedMessage);

            RequestTypeConstant requestType = RequestTypeConstant.values()[obj.getInt("requestTypeConstant")];
            JSONObject requestDataJsonObject = null;
            String requestData = "";

            if (obj.has("requestData")) {
                requestDataJsonObject = obj.getJSONObject("requestData");
                requestData = requestDataJsonObject.toString();
            }

            ProcessedRequest processedRequest = new ProcessedRequest(session, requestType, requestDataJsonObject, requestData);
            mainController.HandleMessage(processedRequest);
        } catch (Exception exception) {
            logger.debug(exception);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.log(Level.DEBUG, "handleTransportError");
        socketSessionService.removeSession(session.getId());
        System.out.println("session " + session.getId() + " error");
        logger.error("session " + session.getId() + " error");
        logger.error("exception socket", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        logger.log(Level.ERROR, "afterConnectionEstablished");
        ResponseObject<ChangeResponseObject> response = new ResponseObject<>(1, new ChangeResponseObject(ChanceHandler.getInstance().generateChance(session.getId())));
        session.sendMessage(new TextMessage(gson.toJson(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.log(Level.DEBUG, "afterConnectionClosed");
        socketSessionService.removeSession(session.getId());
        System.out.println("session " + session.getId() + " closed");
        logger.error("session " + session.getId() + " error");
        super.afterConnectionClosed(session, status);
    }
}
