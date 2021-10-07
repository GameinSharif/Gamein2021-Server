package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetProvidersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController
{
    private final UserController userController;
    private final ProviderController providerController;
    private final GameDataController gameDataController;
    private final Gson gson;

    @Autowired
    public MainController(UserController userController, ProviderController providerController, GameDataController gameDataController)
    {
        this.gson = new Gson();
        this.userController = userController;
        this.gameDataController = gameDataController;
        this.providerController = providerController;
    }

    public void HandleMessage(ProcessedRequest processedRequest)
    {
        String requestData = processedRequest.requestData;
        JSONObject obj = new JSONObject(requestData);
        RequestTypeConstant requestType = RequestTypeConstant.values()[obj.getInt("requestTypeConstant")];

        switch (requestType)
        {
            case LOGIN:
                LoginRequest loginRequest = gson.fromJson(requestData, LoginRequest.class);
                userController.authenticate(processedRequest, loginRequest);
                break;
            case NEW_PROVIDER:
                NewProviderRequest newProviderRequest = gson.fromJson(requestData, NewProviderRequest.class);
                providerController.newProvider(processedRequest, newProviderRequest);
                break;
            case GET_PROVIDERS:
                GetProvidersRequest getProvidersRequest = gson.fromJson(requestData, GetProvidersRequest.class);
                providerController.getProviders(processedRequest, getProvidersRequest);
                break;
            case NEW_OFFER:
                //TODO
                break;
            case GET_OFFERS:
                //TODO
                break;
            case GET_GAME_DATA:
                GetGameDataRequest getGameDataRequest = gson.fromJson(requestData, GetGameDataRequest.class);
                gameDataController.getGameData(processedRequest, getGameDataRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
