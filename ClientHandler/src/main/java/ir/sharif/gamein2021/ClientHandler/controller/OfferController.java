package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.CheatingException;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.OfferService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import ir.sharif.gamein2021.core.util.Enums.TransportNodeType;
import ir.sharif.gamein2021.core.util.Enums.VehicleType;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class OfferController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final ModelMapper modelMapper;
    private PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final OfferService offerService;
    private final UserService userService;
    private final TeamService teamService;
    private final TransportManager transportManager;
    private final Gson gson = new Gson();

    public void handleGetOffers(ProcessedRequest request, GetOffersRequest getOffersRequest) {
        GetOffersResponse getOffersResponse;
        try {
            List<OfferDto> myTeamOffers = offerService.findByTeam(teamService.findTeamById(userService.loadById(request.playerId).getTeamId()));
            List<OfferDto> otherTeamsOffers = offerService.findOffersExceptTeam(teamService.findTeamById(userService.loadById(request.playerId).getTeamId()));
            List<OfferDto> acceptedOffersByMyTeam = offerService.findAcceptedOffers(userService.loadById(request.playerId).getTeamId());

            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, myTeamOffers, otherTeamsOffers, acceptedOffersByMyTeam);
        } catch (Exception e) {
            logger.debug(e);
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, null, null, null);
        }
        pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(getOffersResponse));
    }

    public void createNewOffer(ProcessedRequest request, NewOfferRequest newOfferRequest) {
        NewOfferResponse newOfferResponse;
        try {
            OfferDto offerDto = newOfferRequest.getOffer();
            offerDto.setOfferStatus(OfferStatus.ACTIVE);
            offerDto.setTeamId(userService.loadById(request.playerId).getTeamId());
            OfferDto savedOfferDto = offerService.addOffer(offerDto);

            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, savedOfferDto);
            pushMessageManager.sendMessageByTeamId(userService.loadById(request.playerId).getTeamId().toString(), gson.toJson(newOfferResponse));
        }
        catch (Exception e) {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null);
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(newOfferResponse));
        }
    }

    public void terminateOffer(ProcessedRequest request, TerminateOfferRequest terminateOfferRequest) {
        TerminateOfferResponse terminateOfferResponse;
        try {
            Integer teamId = userService.loadById(request.playerId).getTeamId();
            int offerTeamId = offerService.findById(terminateOfferRequest.getOfferId()).getTeamId();
            if (teamId != offerTeamId) {
                throw new CheatingException();
            }
            OfferDto offerDto = offerService.findById(terminateOfferRequest.getOfferId());
            offerDto.setOfferStatus(OfferStatus.TERMINATED);
            offerService.saveOrUpdate(offerDto);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, terminateOfferRequest.getOfferId());
            pushMessageManager.sendMessageByTeamId(teamId.toString(), gson.toJson(terminateOfferResponse));
        } catch (CheatingException ch) {
            //TODO cheating response
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(terminateOfferResponse));
        } catch (Exception e) {
            logger.debug(e);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(terminateOfferResponse));
        }
    }

    public void acceptOffer(ProcessedRequest request, AcceptOfferRequest acceptOfferRequest) {
        AcceptOfferResponse acceptOfferResponse;
        try {
            OfferDto acceptedOffer = offerService.findById(acceptOfferRequest.getOfferId());
            Team accepterTeam = teamService.findTeamById(userService.loadById(request.playerId).getTeamId());
            Team acceptedTeam = teamService.findTeamById(offerService.findById(acceptOfferRequest.getOfferId()).getTeamId());
            float totalPayment = acceptedOffer.getVolume() * acceptedOffer.getCostPerUnit();
            //TODO add transport cost
            if (acceptedTeam.getId().equals(accepterTeam.getId())) {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "Come on!");
            }
            else if (acceptedOffer.getOfferStatus() != OfferStatus.ACTIVE || acceptedOffer.getOfferDeadline().isBefore(gameCalendar.getCurrentDate()))
            {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "The Offer is not valid");
            }
            else if (totalPayment > acceptedTeam.getCredit()) {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "The Offer Placer Team doesn't have enough money!");
            } else {
                //TODO: Check the Storage of ACCEPTER!
                acceptedTeam.setCredit(acceptedTeam.getCredit() - totalPayment);
                acceptedOffer.setOfferStatus(OfferStatus.ACCEPTED);
                acceptedOffer.setAccepterTeamId(accepterTeam.getId());
                offerService.saveOrUpdate(acceptedOffer);

                teamService.saveOrUpdate(modelMapper.map(acceptedTeam, TeamDto.class));

                transportManager.createTransport(
                        VehicleType.TRUCK,
                        TransportNodeType.FACTORY,
                        accepterTeam.getFactoryId(),
                        TransportNodeType.FACTORY,
                        acceptedTeam.getFactoryId(),
                        gameCalendar.getCurrentDate(),
                        true,
                        acceptedOffer.getProductId(),
                        acceptedOffer.getVolume()
                );
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, acceptedOffer, "Offer Accepted!");
            }
        } catch (Exception e) {
            logger.debug(e);
            acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "An Error Occurred!");
        }

        if (acceptOfferResponse.getAcceptedOffer() == null) {
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(acceptOfferResponse));
        } else {
            pushMessageManager.sendMessageByTeamId(offerService.findById(acceptOfferRequest.getOfferId()).getTeamId().toString(), gson.toJson(acceptOfferResponse));
            pushMessageManager.sendMessageByTeamId(userService.loadById(request.playerId).getTeamId().toString(), gson.toJson(acceptOfferResponse));
        }

    }

}
