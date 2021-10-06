package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.GetContractsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController {
    private final UserController userController;
    private final GameDataController gameDataController;
    private final ContractController contractController;
    private final Gson gson;

    @Autowired
    public MainController(UserController userController, GameDataController gameDataController, ContractController contractController)
    {
        this.gson = new Gson();
        this.userController = userController;
        this.gameDataController = gameDataController;
        this.contractController = contractController;
    }

    public void HandleMessage(ProcessedRequest processedRequest) {
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
            case GET_CONTRACTS:
                GetContractsRequest getContractsRequest = gson.fromJson(requestData, GetContractsRequest.class);
                contractController.getContracts(processedRequest, getContractsRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
