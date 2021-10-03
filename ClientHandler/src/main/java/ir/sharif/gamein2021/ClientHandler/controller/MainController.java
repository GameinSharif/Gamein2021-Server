package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import ir.sharif.gamein2021.core.manager.clientConnection.PushMessageManagerInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private final UserController userController;
    private final Gson gson;
    private final PushMessageManagerInterface pushMessageManager;

    @Autowired
    public MainController(UserController userController, PushMessageManagerInterface pushMessageManager) {
        this.pushMessageManager = pushMessageManager;
        this.gson = new Gson();
        this.userController = userController;
    }

    public void HandleMessage(ProcessedRequest processedRequest) {
        pushMessageManager.sendMessageToAll(processedRequest.requestData);

        String requestData = processedRequest.requestData;
        JSONObject obj = new JSONObject(requestData);
        RequestTypeConstant requestType = RequestTypeConstant.values()[obj.getInt("requestTypeConstant")];

        switch (requestType) {
            case LOGIN:
                LoginRequest loginRequest = gson.fromJson(requestData, LoginRequest.class);
                userController.authenticate(processedRequest, loginRequest);
                break;
            case NEW_OFFER:
                //TODO
                break;
            case GET_OFFERS:
                //TODO
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}