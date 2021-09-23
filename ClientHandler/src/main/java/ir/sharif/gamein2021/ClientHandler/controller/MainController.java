package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetOffersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetTeamOffersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewOfferRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.TerminateOfferRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController
{
    private final UserController userController;
    private final OfferController offerController;
    private final Gson gson;

    @Autowired
    public MainController(UserController userController, OfferController offerController)
    {
        this.gson = new Gson();
        this.userController = userController;
        this.offerController = offerController;
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
                NewOfferRequest newOfferRequest = gson.fromJson(requestData, NewOfferRequest.class);
                offerController.createNewOffer(processedRequest, newOfferRequest);
                break;
            case GET_OFFERS:
                GetOffersRequest getOffersRequest = gson.fromJson(requestData, GetOffersRequest.class);
                offerController.handleGetOffers(processedRequest, getOffersRequest);
                break;
            case GET_TEAM_OFFERS:
                GetTeamOffersRequest getTeamOffersRequest = gson.fromJson(requestData, GetTeamOffersRequest.class);
                offerController.handleGetTeamOffers(processedRequest, getTeamOffersRequest);
                break;
            case TERMINATE_OFFER:
                TerminateOfferRequest terminateOfferRequest = gson.fromJson(requestData, TerminateOfferRequest.class);
                offerController.terminateOffer(processedRequest, terminateOfferRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
