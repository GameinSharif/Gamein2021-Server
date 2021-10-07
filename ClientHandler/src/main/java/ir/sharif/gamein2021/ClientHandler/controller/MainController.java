package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.GetContractsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.EditNegotiationCostPerUnitRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetNegotiationsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewNegotiationRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetProvidersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderRequest;
import ir.sharif.gamein2021.ClientHandler.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MainController {
    private final UserController userController;
    private final GameDataController gameDataController;
    private final ContractController contractController;
    private final NegotiationController negotiationController;
    private final ProviderController providerController;
    private final Gson gson;

    @Autowired
    public MainController(UserController userController, GameDataController gameDataController, ContractController contractController, NegotiationController negotiationController, ProviderController providerController)
    {
        this.gson = new Gson();
        this.userController = userController;
        this.gameDataController = gameDataController;
        this.contractController = contractController;
        this.negotiationController = negotiationController;
        this.providerController = providerController;
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
            case GET_NEGOTIATIONS:
                GetNegotiationsRequest getNegotiationsRequest = gson.fromJson(requestData, GetNegotiationsRequest.class);
                negotiationController.getNegotiations( processedRequest, getNegotiationsRequest);
                break;
            case NEW_NEGOTIATION:
                NewNegotiationRequest newNegotiationRequest = gson.fromJson(requestData, NewNegotiationRequest.class);
                negotiationController.newNegotiation(processedRequest, newNegotiationRequest);
                break;
            case EDIT_NEGOTIATION_COST_PER_UNIT:
                EditNegotiationCostPerUnitRequest editRequest = gson.fromJson(requestData, EditNegotiationCostPerUnitRequest.class);
                negotiationController.editNegotiationCostPerUnit(processedRequest, editRequest);
                break;
            case NEW_PROVIDER:
                NewProviderRequest newProviderRequest = gson.fromJson(requestData, NewProviderRequest.class);
                providerController.newProvider(processedRequest, newProviderRequest);
                break;
            case GET_PROVIDERS:
                GetProvidersRequest getProvidersRequest = gson.fromJson(requestData, GetProvidersRequest.class);
                providerController.getProviders(processedRequest, getProvidersRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
