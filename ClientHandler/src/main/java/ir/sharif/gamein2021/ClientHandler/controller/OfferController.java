package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.OfferService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.exception.CheatingException;
import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferController
{

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final OfferService offerService;
    private final UserService userService;
    private final TeamService teamService;
    private final Gson gson = new Gson();

    public OfferController(LocalPushMessageManager pushMessageManager, OfferService offerService, UserService userService, TeamService teamService) {
        this.pushMessageManager = pushMessageManager;
        this.offerService = offerService;
        this.userService = userService;
        this.teamService = teamService;
    }

    public void handleGetOffers(ProcessedRequest request, GetOffersRequest getOffersRequest)
    {
        GetOffersResponse getOffersResponse;
        try {
            List<OfferDto> myTeamOffers = offerService.findByTeam(teamService.findTeamById(userService.loadById(getOffersRequest.playerId).getTeamId()));
            List<OfferDto> otherTeamsOffers = offerService.findOffersExceptTeam(teamService.findTeamById(userService.loadById(getOffersRequest.playerId).getTeamId()));

            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, myTeamOffers, otherTeamsOffers);
        } catch (Exception e) {
            logger.debug(e);
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, null, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getOffersResponse));
    }

    public void createNewOffer(ProcessedRequest request, NewOfferRequest newOfferRequest)
    {
        NewOfferResponse newOfferResponse;
        try {
            OfferDto offerDto = newOfferRequest.getOffer();
            offerDto.setOfferStatus(OfferStatus.ACTIVE);
            offerDto.setTeamId(userService.loadById(newOfferRequest.playerId).getTeamId());
            OfferDto savedOfferDto = offerService.addOffer(offerDto);

            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, savedOfferDto);
        } catch (Exception e) {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newOfferResponse));
    }

    public void terminateOffer(ProcessedRequest request, TerminateOfferRequest terminateOfferRequest)
    {
        TerminateOfferResponse terminateOfferResponse;
        try {
            int teamId = userService.loadById(terminateOfferRequest.playerId).getTeamId();
            int offerTeamId = offerService.findById(terminateOfferRequest.getOfferId()).getTeamId();
            if (teamId != offerTeamId) {
                throw new CheatingException();
            }
            OfferDto offerDto = offerService.findById(terminateOfferRequest.getOfferId());
            offerDto.setOfferStatus(OfferStatus.TERMINATED);
            offerService.saveOrUpdate(offerDto);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, terminateOfferRequest.getOfferId());
        } catch (CheatingException ch) {
            //TODO cheating response
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
        } catch (Exception e) {
            logger.debug(e);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateOfferResponse));
    }
}
