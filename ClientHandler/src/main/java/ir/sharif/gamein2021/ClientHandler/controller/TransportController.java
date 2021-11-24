package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.GetTeamTransportsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.GetTeamTransportsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.StartTransportForPlayerStoragesRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.StartTransportForPlayerStoragesResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.exception.NotEnoughCapacityException;
import ir.sharif.gamein2021.core.exception.NotEnoughMoneyException;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@AllArgsConstructor
@Component
public class TransportController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());
    private final PushMessageManagerInterface pushMessageManager;
    private final TransportService transportService;
    private final UserService userService;
    private final GameCalendar gameCalendar;
    private final StorageService storageService;
    private final DcService dcService;
    private final TeamService teamService;
    private final TransportManager transportManager;
    private final Gson gson = new Gson();

    public void getTeamTransports(ProcessedRequest processedRequest, GetTeamTransportsRequest getTeamTransportsRequest) {
        int playerId = processedRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Integer teamId = user.getTeamId();
        ArrayList<TransportDto> transportDtos = transportService.getTransportsByTeam(teamId);
        GetTeamTransportsResponse response = new GetTeamTransportsResponse(ResponseTypeConstant.GET_TEAM_TRANSPORTS, transportDtos);
        pushMessageManager.sendMessageByUserId(user.getId().toString(), gson.toJson(response));
    }

    public void startTransportForPlayersStorages(ProcessedRequest processedRequest, StartTransportForPlayerStoragesRequest request) {
        int playerId = processedRequest.playerId;
        StartTransportForPlayerStoragesResponse response;
        try {
            UserDto user = userService.loadById(playerId);
            Integer teamId = user.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            checkIfTransportSourceHasEnoughProduct(request);
            checkSourceAndDestinationIsForTeam(teamId, request);
            checkDestinationCapacity(request);
            checkTeamMoney(request, teamDto);
            TransportDto transportDto = transportManager.createTransport(
                    request.getVehicleType(),
                    request.getSourceType(),
                    request.getSourceId(),
                    request.getDestinationType(),
                    request.getDestinationId(),
                    gameCalendar.getCurrentDate(),
                    request.isHasInsurance(),
                    request.getProductId(),
                    request.getAmount());
            response = new StartTransportForPlayerStoragesResponse(ResponseTypeConstant.TRANSPORT_TO_STORAGE, transportDto , "success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response = new StartTransportForPlayerStoragesResponse(ResponseTypeConstant.TRANSPORT_TO_STORAGE, null , e.getMessage());
            logger.debug(e);
        }
        pushMessageManager.sendMessageByTeamId(userService.loadById(playerId).getTeamId().toString(), gson.toJson(response));
    }

    private void checkTeamMoney(StartTransportForPlayerStoragesRequest request, TeamDto team) {
        int distance = transportManager.getTransportDistance(request.getSourceType(), request.getSourceId(), request.getDestinationType(), request.getDestinationId(), request.getVehicleType());
        float transportCost = transportManager.calculateTransportCost(
                request.getVehicleType(),
                distance,
                request.getProductId(),
                request.getAmount(),
                request.isHasInsurance()
        );
        if(team.getCredit() < transportCost)
            throw new NotEnoughMoneyException("You don't have enough credit to start this transport !");
        else
        {
            team.setCredit(team.getCredit() - transportCost);
            team.setWealth(team.getWealth() - transportCost);
            team.setTransportationCost(team.getTransportationCost() + transportCost);
            
            teamService.saveOrUpdate(team);
        }
    }

    private void checkDestinationCapacity(StartTransportForPlayerStoragesRequest request) {
        Product product = ReadJsonFilesManager.findProductById(request.getProductId());
        if (storageService.calculateTransportCapacity(
                request.getDestinationId(),
                isDc(request.getDestinationType()),
                product.getProductType()) <
                request.getAmount() * product.getVolumetricUnit())
            throw new NotEnoughCapacityException("You dont have enough space to start this transport.");
    }

    private void checkSourceAndDestinationIsForTeam(Integer teamId, StartTransportForPlayerStoragesRequest request) {
        int sourceTeamId = findTeamByStorage(request.getSourceId(), request.getSourceType());
        int destinationTeamId = findTeamByStorage(request.getDestinationId(), request.getDestinationType());
        if (!(sourceTeamId == teamId && destinationTeamId == teamId))
            throw new InvalidRequestException("Both source and destination storages must be yours!");
        if (request.getSourceId() == request.getDestinationId() && request.getSourceType() == request.getDestinationType())
            throw new InvalidRequestException("These two storages are the same!");
    }

    private void checkIfTransportSourceHasEnoughProduct(StartTransportForPlayerStoragesRequest request) {
        StorageProductDto storageProductDto = storageService.getStorageProductWithBuildingId(request.getSourceId(), isDc(request.getSourceType()), request.getProductId());
        if (storageProductDto == null || storageProductDto.getAmount() < request.getAmount())
            throw new InvalidRequestException("You don't have enough of this product !");
    }

    private Integer findTeamByStorage(Integer buildingId, Enums.TransportNodeType type) {
        if (type.equals(Enums.TransportNodeType.DC)) {
            DcDto dc = dcService.loadById(buildingId);
            Assert.notNull(dc, "Invalid dc id!");
            return dc.getOwnerId();
        } else if (type.equals(Enums.TransportNodeType.FACTORY)) {
            return teamService.findTeamIdByFactoryId(buildingId);
        } else {
            throw new InvalidRequestException("Invalid transport type for source or destination!");
        }
    }

    private boolean isDc(Enums.TransportNodeType transportNodeType) {
        if (transportNodeType.equals(Enums.TransportNodeType.DC))
            return true;
        return false;
    }


}
