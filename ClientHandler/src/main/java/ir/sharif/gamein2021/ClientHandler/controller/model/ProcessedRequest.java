package ir.sharif.gamein2021.ClientHandler.controller.model;

import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public class ProcessedRequest {

    public WebSocketSession session;
    public RequestTypeConstant requestType;
    public JSONObject requestDataJsonObject;
    public String requestData;

    public ProcessedRequest(WebSocketSession session, RequestTypeConstant requestType, JSONObject requestDataJsonObject, String requestData) {

        this.session = session;
        this.requestType = requestType;
        this.requestDataJsonObject = requestDataJsonObject;
        this.requestData = requestData;
    }
}
