package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetNegotiationsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewNegotiationRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController
{
    private final UserController userController;
    private final NegotiationController negotiationController;
    private final GameDataController gameDataController;
    private final Gson gson;
    @Autowired
    public MainController(UserController userController, NegotiationController negotiationController, GameDataController gameDataController)
    {
        this.gson = new Gson();
        this.userController = userController;
        this.negotiationController = negotiationController;
        this.gameDataController = gameDataController;
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
            case NEW_OFFER:
                //TODO
                break;
            case GET_OFFERS:
                //TODO
                break;
            case GET_NEGOTIATIONS:
                GetNegotiationsRequest getNegotiationsRequest = gson.fromJson(requestData, GetNegotiationsRequest.class);
                negotiationController.getNegotiations( processedRequest, getNegotiationsRequest);
                break;
            case NEW_NEGOTIATION:
                NewNegotiationRequest newNegotiationRequest = gson.fromJson(requestData, NewNegotiationRequest.class);
                negotiationController.newNegotiation(newNegotiationRequest);
            case GET_GAME_DATA:
                GetGameDataRequest getGameDataRequest = gson.fromJson(requestData, GetGameDataRequest.class);
                gameDataController.getGameData(processedRequest, getGameDataRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
