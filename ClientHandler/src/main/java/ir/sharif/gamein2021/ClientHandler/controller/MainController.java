package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetOffersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewOfferRequest;
import ir.sharif.gamein2021.ClientHandler.domain.authentication.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.service.ServiceRepository;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class MainController
{

    private final ServiceRepository serviceRepository;
    private final Gson gson;

    public MainController(ServiceRepository serviceRepository)
    {
        this.serviceRepository = serviceRepository;
        this.gson = new Gson();
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
                serviceRepository.loginService.authenticate(processedRequest, loginRequest);
                break;

            case NEW_OFFER:
                NewOfferRequest newOfferRequest = gson.fromJson(requestData, NewOfferRequest.class);
                serviceRepository.rfqService.newOffer(processedRequest, newOfferRequest);
                break;

            case GET_OFFERS:
                GetOffersRequest getOffersRequest = gson.fromJson(requestData, GetOffersRequest.class);
                serviceRepository.rfqService.getOffers(processedRequest, getOffersRequest);
                break;

            default:
                System.out.println("Request type is invalid.");
        }
    }
}
