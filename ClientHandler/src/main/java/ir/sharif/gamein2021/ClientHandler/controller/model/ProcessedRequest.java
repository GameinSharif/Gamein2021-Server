package ir.sharif.gamein2021.ClientHandler.controller.model;

import org.springframework.web.socket.WebSocketSession;

public class ProcessedRequest {

    public WebSocketSession session;
    public String requestData;

    public ProcessedRequest(WebSocketSession session, String requestData) {

        this.session = session;
        this.requestData = requestData;
    }
}
