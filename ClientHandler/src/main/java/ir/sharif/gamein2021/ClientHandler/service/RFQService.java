package ir.sharif.gamein2021.ClientHandler.service;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.OfferController;
import ir.sharif.gamein2021.ClientHandler.controller.UserController;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.User;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RFQService {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final OfferController offerController;
    private final UserController userController;
    private final PushMessageService pushMessageService;
    private final Gson gson = new Gson();

    public RFQService(OfferController offerController, UserController userController, PushMessageService pushMessageService) {
        this.offerController = offerController;
        this.userController = userController;
        this.pushMessageService = pushMessageService;
    }

    public void newOffer(ProcessedRequest request, NewOfferRequest newOfferRequest) {

        OfferDto offer = newOfferRequest.getOffer();

        NewOfferResponse newOfferResponse;
        try {
            if (offerController.newOffer(userController.getUser(newOfferRequest.playerId).getTeam(), offer).equals("DONE")) {
                newOfferResponse = new NewOfferResponse(
                        ResponseTypeConstant.NEW_OFFER,
                        "Offer submitted successfully!"
                );
            } else {
                newOfferResponse = new NewOfferResponse(
                        ResponseTypeConstant.NEW_OFFER,
                        "Failed to submit the offer!"
                );
            }
        } catch (Exception e) {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(
                    ResponseTypeConstant.NEW_OFFER,
                    "Server ERROR, please try later!"
            );
        }

        pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(newOfferResponse));

    }

    public void getOffers(ProcessedRequest request, GetOffersRequest getOffersRequest) {

        User user = userController.getUser(getOffersRequest.playerId);
        ArrayList<GetOffersTransitModel> offers = offerController.getOffers();

        try {

            GetOffersResponse getOffersResponse = new GetOffersResponse(
                    ResponseTypeConstant.GET_OFFERS,
                    offers
            );
            pushMessageService.sendMessageBySessionId(request.session.getId(), gson.toJson(getOffersResponse));

        } catch (Exception e) {
            logger.debug(e);
        }

    }

}
