package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.GetStorageProductsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.GetStorageProductsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.Product.RemoveProductRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.RemoveProductResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.StorageService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ProductController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final Gson gson;
    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;
    private final StorageService storageService;
    private final DcService dcService;

    public void removeProduct(ProcessedRequest request, RemoveProductRequest removeProductRequest)
    {
        RemoveProductResponse response;
        try
        {
            TeamDto teamDto = teamService.loadById(request.teamId);
            checkTeamAndStorage(removeProductRequest.getBuildingId(), removeProductRequest.isDc(), teamDto);
            StorageDto storageDto;
            storageDto = storageService.deleteProducts(
                    removeProductRequest.getBuildingId(),
                    removeProductRequest.isDc(),
                    removeProductRequest.getProductId(),
                    removeProductRequest.getAmount());
            response = new RemoveProductResponse(ResponseTypeConstant.REMOVE_PRODUCT, storageDto, "success");
            pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(response));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new RemoveProductResponse(ResponseTypeConstant.REMOVE_PRODUCT, null, e.getMessage());
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(response));
        }
    }

    public void getStorages(ProcessedRequest request, GetStorageProductsRequest getStorageProductsRequest)
    {
        GetStorageProductsResponse response;
        try
        {
            TeamDto teamDto = teamService.loadById(request.teamId);
            List<StorageDto> storages = storageService.findAllStorageForTeam(teamDto);
            response = new GetStorageProductsResponse(ResponseTypeConstant.GET_STORAGES, storages, "success");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new GetStorageProductsResponse(ResponseTypeConstant.GET_STORAGES, null, e.getMessage());
        }
        pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(response));
    }

    private void checkTeamAndStorage(Integer buildingId, boolean isDc, TeamDto teamDto)
    {
        if (isDc)
        {
            DcDto dc = dcService.loadById(buildingId);
            if (!dc.getOwnerId().equals(teamDto.getId()))
            {
                throw new InvalidRequestException("You don't have access to this dc.");
            }
        }
        else
        {
            if (!teamDto.getFactoryId().equals(buildingId))
            {
                throw new InvalidRequestException("You don't have access to this factory.");
            }
        }
    }
}
