package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.StorageService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ProductController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final Gson gson;
    private final LocalPushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;
    private final StorageService storageService;
    private final DcService dcService;

    public void addProduct(ProcessedRequest request, AddProductRequest addProductRequest) {
        Integer id = request.playerId;
        AddProductResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            checkTeamAndStorage(addProductRequest.getBuildingId(), addProductRequest.isDc(), teamDto);
            StorageDto storageDto;
            storageDto = storageService.addProduct(
                    addProductRequest.getBuildingId(),
                    addProductRequest.isDc(),
                    addProductRequest.getProductId(),
                    addProductRequest.getAmount());
            response = new AddProductResponse(ResponseTypeConstant.ADD_PRODUCT, storageDto, "success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new AddProductResponse(ResponseTypeConstant.ADD_PRODUCT, null, e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }

    public void removeProduct(ProcessedRequest request, RemoveProductRequest removeProductRequest) {
        Integer id = request.playerId;
        RemoveProductResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            checkTeamAndStorage(removeProductRequest.getBuildingId(), removeProductRequest.isDc(), teamDto);
            StorageDto storageDto;
            storageDto = storageService.deleteProducts(
                    removeProductRequest.getBuildingId(),
                    removeProductRequest.isDc(),
                    removeProductRequest.getProductId(),
                    removeProductRequest.getAmount());
            response = new RemoveProductResponse(ResponseTypeConstant.REMOVE_PRODUCT, storageDto, "success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new RemoveProductResponse(ResponseTypeConstant.REMOVE_PRODUCT, null, e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }

    public void getStorages(ProcessedRequest request, GetStorageProductsRequest getStorageProductsRequest) {
        Integer id = request.playerId;
        GetStorageProductsResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            List<StorageDto> storages = storageService.findAllStorageForTeam(teamDto);
            response = new GetStorageProductsResponse(ResponseTypeConstant.GET_STORAGES, storages, "success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new GetStorageProductsResponse(ResponseTypeConstant.GET_STORAGES, null, e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }

    private void checkTeamAndStorage(Integer buildingId, boolean isDc, TeamDto teamDto) {
        if (isDc) {
            DcDto dc = dcService.loadById(buildingId);
            if (dc.getOwnerId() != teamDto.getId())
                throw new InvalidRequestException("You don't have access to this dc.");
        } else {
            if (teamDto.getFactoryId() != buildingId)
                throw new InvalidRequestException("You don't have access to this factory.");
        }
    }
}
