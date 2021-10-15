package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.RequestDtoConversionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.Service.OfferService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.exception.CheatingException;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferController {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final RequestDtoConversionManager requestDtoConversionManager;
    private final OfferService offerService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public OfferController(
            PushMessageManager pushMessageManager, RequestDtoConversionManager requestDtoConversionManager,
            OfferService offerService, UserService userService
    ) {
        this.pushMessageManager = pushMessageManager;
        this.requestDtoConversionManager = requestDtoConversionManager;
        this.offerService = offerService;
        this.userService = userService;
    }

    public void handleGetOffers(ProcessedRequest request, GetOffersRequest getOffersRequest) {
        GetOffersResponse getOffersResponse;
        try
        {
            List<OfferDto> offerDtos = offerService.getAllOffers();
            List<OfferDto> showingOfferDtos = new ArrayList<>();
            for (OfferDto offerDto : offerDtos) {
                if (!offerDto.getTeamId().equals(
                        userService.findById(getOffersRequest.playerId).getTeam().getId())
                ) showingOfferDtos.add(offerDto);
            }
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, showingOfferDtos);
        }
        catch (Exception e)
        {
            logger.debug(e);
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getOffersResponse));
    }

    public void handleGetTeamOffers(ProcessedRequest request, GetTeamOffersRequest getTeamOffersRequest) {
        GetTeamOffersResponse getTeamOffersResponse;
        try
        {
            List<OfferDto> offerDtos = offerService.getTeamOffers(
                    userService.findById(getTeamOffersRequest.playerId).getTeam().getId()
            );
            getTeamOffersResponse = new GetTeamOffersResponse(ResponseTypeConstant.GET_TEAM_OFFERS, offerDtos);
        }
        catch (Exception e)
        {
            logger.debug(e);
            getTeamOffersResponse = new GetTeamOffersResponse(ResponseTypeConstant.GET_TEAM_OFFERS, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getTeamOffersResponse));
    }

    public void createNewOffer(ProcessedRequest request, NewOfferRequest newOfferRequest) {
        NewOfferResponse newOfferResponse;
        try
        {
            OfferDto offerDto = offerService.save(newOfferRequest.getOfferDto());
            System.out.println(offerDto);

            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, "Your Offer Submitted Successfully!");
        }
        catch (Exception e)
        {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, "Unable to Submit Your Offer!");
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newOfferResponse));
    }

    public void terminateOffer(ProcessedRequest request, TerminateOfferRequest terminateOfferRequest) {
        TerminateOfferResponse terminateOfferResponse;
        try
        {
            if (!userService.findById(terminateOfferRequest.playerId).getTeam().getId().equals(
                    offerService.findById(terminateOfferRequest.getOfferId()).getTeam().getId())
            ) {
                throw new CheatingException();
            }
            offerService.delete(terminateOfferRequest.getOfferId());
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, "The Offer Terminated Successfully!");
        }
        catch (CheatingException ch) {
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, "You Idiot!");

            // ban like shit
        }
        catch (Exception e)
        {
            logger.debug(e);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, "Unable to Terminate the Offer!");
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateOfferResponse));
    }
}
