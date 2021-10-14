package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.request.BidForAuctionRequest;
import ir.sharif.gamein2021.ClientHandler.request.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MainController {
    private final UserController userController;
    private final GameDataController gameDataController;
    private final TeamController teamController;
    private final AuctionController auctionController;
    private final Gson gson;


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
            case GET_RANDOM_COUNTRY:
                teamController.setRandomCountryForTeam(processedRequest);
            case BID_FOR_AUCTION:
                BidForAuctionRequest bidForAuctionRequest = gson.fromJson(requestData, BidForAuctionRequest.class);
                auctionController.addBidForAuction(processedRequest, bidForAuctionRequest);
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
