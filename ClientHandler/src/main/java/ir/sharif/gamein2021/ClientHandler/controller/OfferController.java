package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.CheatingException;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import ir.sharif.gamein2021.core.util.Enums.TransportNodeType;
import ir.sharif.gamein2021.core.util.Enums.VehicleType;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ProductionLineService productionLineService;
    private final StorageService storageService;
    private final TransportManager transportManager;
    private final Gson gson = new Gson();

    public void handleGetOffers(ProcessedRequest request, GetOffersRequest getOffersRequest)
    {
        GetOffersResponse getOffersResponse;
        try
        {
            List<OfferDto> myTeamOffers = offerService.findByTeam(teamService.findTeamById(userService.loadById(request.playerId).getTeamId()));
            List<OfferDto> otherTeamsOffers = offerService.findOffersExceptTeam(teamService.findTeamById(userService.loadById(request.playerId).getTeamId()));
            List<OfferDto> acceptedOffersByMyTeam = offerService.findAcceptedOffers(userService.loadById(request.playerId).getTeamId());

            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, myTeamOffers, otherTeamsOffers, acceptedOffersByMyTeam);
        } catch (Exception e)
        {
            logger.debug(e);
            getOffersResponse = new GetOffersResponse(ResponseTypeConstant.GET_OFFERS, null, null, null);
        }
        pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(getOffersResponse));
    }

    public void createNewOffer(ProcessedRequest request, NewOfferRequest newOfferRequest)
    {
        NewOfferResponse newOfferResponse;
        try
        {
            float totalPayment = newOfferRequest.getOffer().getVolume() * newOfferRequest.getOffer().getCostPerUnit();
            TeamDto teamDto = teamService.loadById(request.teamId);
            int minPrice = ReadJsonFilesManager.findProductById(newOfferRequest.getOffer().getProductId()).getMinPrice();
            int maxPrice = ReadJsonFilesManager.findProductById(newOfferRequest.getOffer().getProductId()).getMaxPrice();
            if (newOfferRequest.getOffer().getVolume() <= 0)
            {
                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "Product Amount is not valid!");
            }
            else if (ReadJsonFilesManager.findProductById(newOfferRequest.getOffer().getProductId()).getProductType() != Enums.ProductType.SemiFinished)
            {
                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "Product is not SemiFinished");
            }
            else if (newOfferRequest.getOffer().getCostPerUnit() > maxPrice || newOfferRequest.getOffer().getCostPerUnit() < minPrice)
            {
                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "The price is not in its range!");
            }
            else if (totalPayment > teamDto.getCredit())
            {
                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "You don't have enough money for creating this offer!");
            }
            else if (!isProductionLineValid(newOfferRequest.getOffer(), teamService.findTeamById(request.teamId)))
            {
                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "You can't use this product in any of your production lines!");
            }
            else
            {
                OfferDto offerDto = newOfferRequest.getOffer();
                offerDto.setOfferStatus(OfferStatus.ACTIVE);
                offerDto.setTeamId(userService.loadById(request.playerId).getTeamId());
                OfferDto savedOfferDto = offerService.addOffer(offerDto);

                newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, savedOfferDto, "OK!");
            }
        } catch (Exception e)
        {
            logger.debug(e);
            newOfferResponse = new NewOfferResponse(ResponseTypeConstant.NEW_OFFER, null, "An Error Occurred!");
        }

        if (newOfferResponse.getOffer() == null)
        {
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newOfferResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(newOfferResponse));
        }
    }

    public void terminateOffer(ProcessedRequest request, TerminateOfferRequest terminateOfferRequest)
    {
        TerminateOfferResponse terminateOfferResponse;
        try
        {
            Integer teamId = userService.loadById(request.playerId).getTeamId();
            int offerTeamId = offerService.findById(terminateOfferRequest.getOfferId()).getTeamId();
            if (teamId != offerTeamId)
            {
                throw new CheatingException();
            }
            OfferDto offerDto = offerService.findById(terminateOfferRequest.getOfferId());
            offerDto.setOfferStatus(OfferStatus.TERMINATED);
            offerService.saveOrUpdate(offerDto);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, terminateOfferRequest.getOfferId());
            pushMessageManager.sendMessageByTeamId(teamId.toString(), gson.toJson(terminateOfferResponse));
        } catch (CheatingException ch)
        {
            //TODO cheating response
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(terminateOfferResponse));
        } catch (Exception e)
        {
            logger.debug(e);
            terminateOfferResponse = new TerminateOfferResponse(ResponseTypeConstant.TERMINATE_OFFER, null);
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(terminateOfferResponse));
        }
    }

    public void acceptOffer(ProcessedRequest request, AcceptOfferRequest acceptOfferRequest)
    {
        AcceptOfferResponse acceptOfferResponse;
        try
        {
            OfferDto acceptedOffer = offerService.findById(acceptOfferRequest.getOfferId());
            Team accepterTeam = teamService.findTeamById(userService.loadById(request.playerId).getTeamId());
            Team acceptedTeam = teamService.findTeamById(offerService.findById(acceptOfferRequest.getOfferId()).getTeamId());

            float totalPayment = acceptedOffer.getVolume() * acceptedOffer.getCostPerUnit();
            totalPayment += transportManager.calculateTransportCost(
                    VehicleType.TRUCK,
                    transportManager.calculateTransportDistance(
                            TransportNodeType.FACTORY,
                            accepterTeam.getFactoryId(),
                            TransportNodeType.FACTORY,
                            acceptedTeam.getFactoryId(),
                            VehicleType.TRUCK),
                    acceptedOffer.getProductId(),
                    acceptedOffer.getVolume()
            );

            if (acceptedTeam.getId().equals(accepterTeam.getId()))
            {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "Come on!");
            }
            else if (acceptedOffer.getOfferStatus() != OfferStatus.ACTIVE || acceptedOffer.getOfferDeadline().isBefore(gameCalendar.getCurrentDate()))
            {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "The Offer is not valid");
            }
            else if (totalPayment > acceptedTeam.getCredit())
            {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "The Offer Placer Team doesn't have enough money!");
            }
            else if (!isAmountOK(acceptedOffer, accepterTeam))
            {
                acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "Your team don't have enough amount of product!");
            }
            //TODO need to check capacity of accepted team? i think not...
            else
            {
                acceptedOffer.setOfferStatus(OfferStatus.ACCEPTED);
                acceptedOffer.setAccepterTeamId(accepterTeam.getId());
                offerService.saveOrUpdate(acceptedOffer);

                acceptedTeam.setCredit(acceptedTeam.getCredit() - totalPayment);
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
        } catch (Exception e)
        {
            logger.debug(e);
            acceptOfferResponse = new AcceptOfferResponse(ResponseTypeConstant.ACCEPT_OFFER, null, "An Error Occurred!");
        }

        if (acceptOfferResponse.getAcceptedOffer() == null)
        {
            pushMessageManager.sendMessageByUserId(userService.loadById(request.playerId).getId().toString(), gson.toJson(acceptOfferResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(offerService.findById(acceptOfferRequest.getOfferId()).getTeamId().toString(), gson.toJson(acceptOfferResponse));
            pushMessageManager.sendMessageByTeamId(userService.loadById(request.playerId).getTeamId().toString(), gson.toJson(acceptOfferResponse));
        }

    }

    private boolean isAmountOK(OfferDto offer, Team team)
    {
        for (StorageDto storageDto : storageService.findAllStorageForTeam(teamService.loadById(team.getId())))
        {
            for (StorageProductDto product : storageDto.getProducts())
            {
                if (product.getId().equals(offer.getProductId()))
                {
                    if (product.getAmount() >= offer.getVolume())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isProductionLineValid(OfferDto offerDto, Team team)
    {
        List<Integer> categories = Arrays.stream(ReadJsonFilesManager.findProductById(offerDto.getProductId()).getCategoryIds().split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(team);
        for (ProductionLineDto productionLineDto : productionLines)
        {
            for (Integer category : categories)
            {
                if (productionLineDto.getProductionLineTemplateId().equals(category) && productionLineDto.getStatus() != Enums.ProductionLineStatus.SCRAPPED)
                {
                    return true;
                }
            }
        }
        return false;
    }
}