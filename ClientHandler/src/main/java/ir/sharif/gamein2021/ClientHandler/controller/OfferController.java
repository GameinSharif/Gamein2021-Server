package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.OfferService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.exception.CheatingException;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class OfferController
{

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final OfferService offerService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public void handleGetOffers(ProcessedRequest request, GetOffersRequest getOffersRequest)
    {
        GetOffersResponse getOffersResponse;
        try
        {
            List<OfferDto> myTeamOffers = offerService.findByTeam(userService.loadById(getOffersRequest.playerId).getTeam());
            List<OfferDto> otherTeamsOffers = offerService.findOffersExceptTeam(userService.loadById(getOffersRequest.playerId).getTeam());

            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, myTeamOffers, otherTeamsOffers);
        } catch (Exception e)
        {
            logger.debug(e);
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, null, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getOffersResponse));
    }

    public void createNewOffer(ProcessedRequest request, NewOfferRequest newOfferRequest)
    {
        NewOfferResponse newOfferResponse;
        try
        {
            OfferDto offerDto = offerService.addOffer(newOfferRequest.getOfferDto());
            System.out.println(offerDto);

            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, offerDto);
        } catch (Exception e)
        {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newOfferResponse));
    }

    public void terminateOffer(ProcessedRequest request, TerminateOfferRequest terminateOfferRequest)
    {
        TerminateOfferResponse terminateOfferResponse;
        try
        {
            int teamId = userService.loadById(terminateOfferRequest.playerId).getTeam().getId();
            int offerTeamId = offerService.findById(terminateOfferRequest.getOfferId()).getTeam().getId();
            if (teamId != offerTeamId)
            {
                throw new CheatingException();
            }
            offerService.delete(terminateOfferRequest.getOfferId());
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, terminateOfferRequest.getOfferId());
        }
        catch (CheatingException ch)
        {
            //TODO cheating response
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
        }
        catch (Exception e)
        {
            logger.debug(e);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateOfferResponse));
    }
}
