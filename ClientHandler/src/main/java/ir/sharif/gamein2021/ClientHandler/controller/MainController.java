package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import ir.sharif.gamein2021.core.manager.clientConnection.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.ClientToEnginePublisherInterface;
import ir.sharif.gamein2021.core.manager.engineConnection.requests.BaseEngineRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController {
    private final UserController userController;
    private final GameDataController gameDataController;
    private final Gson gson;
    private final PushMessageManagerInterface pushMessageManager;
    private final ClientToEnginePublisherInterface clientToEnginePublisher;

    @Autowired
    public MainController(UserController userController, GameDataController gameDataController, PushMessageManagerInterface pushMessageManager, ClientToEnginePublisherInterface clientToEnginePublisher)
    {
        this.pushMessageManager = pushMessageManager;
        this.clientToEnginePublisher = clientToEnginePublisher;
        this.gson = new Gson();
        this.userController = userController;
        this.gameDataController = gameDataController;
    }

    public void HandleMessage(ProcessedRequest processedRequest) {
        pushMessageManager.sendMessageToAll(processedRequest.requestData);
        clientToEnginePublisher.send(new BaseEngineRequest(processedRequest.requestData));
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
            case GET_GAME_DATA:
                gameDataController.getGameData(processedRequest);
                gameDataController.getCurrentWeekDemands(processedRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
