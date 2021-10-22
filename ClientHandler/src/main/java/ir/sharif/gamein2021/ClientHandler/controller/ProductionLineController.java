package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineIdException;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineTemplateIdException;
import ir.sharif.gamein2021.core.service.ProductionLineService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.Enums;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductionLineController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final UserService userService;
    private final ProductionLineService productionLineService;

    private final Gson gson = new Gson();

    public ProductionLineController(LocalPushMessageManager pushMessageManager, UserService userService, ProductionLineService productionLineService) {
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.productionLineService = productionLineService;
    }

    public void GetProductionLines(ProcessedRequest processedRequest, GetProductionLinesRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = user.getTeam();

        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(userTeam);

        GetProductionLinesResponse response = new GetProductionLinesResponse(productionLines);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }

    public void constructProductionLine(ProcessedRequest processedRequest, ConstructProductionLineRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        ProductionLineDto productionLine = new ProductionLineDto();

        productionLine.setStatus(Enums.ProductionLineStatus.ACTIVE);
        productionLine.setProductionLineTemplateId(request.getProductionLineTemplateId());
        productionLine.setTeamId(user.getTeam().getId());
        productionLine.setEfficiencyLevel(0);
        productionLine.setQualityLevel(0);

        try {
            ProductionLineDto createdProductionLine = productionLineService.CreateProductionLine(productionLine);
            ConstructProductionLineResponse response = new ConstructProductionLineResponse(createdProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (InvalidProductionLineTemplateIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }

    public void scrapProductionLine(ProcessedRequest processedRequest, ScrapProductionLineRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        try {
            ProductionLineDto savedProductionLine = productionLineService.scrapProductionLine(user.getTeam(), request.getProductionLineId());
            ScrapProductionLineResponse response = new ScrapProductionLineResponse(savedProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }

    public void StartProduction(ProcessedRequest processedRequest, StartProductionRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        try {
            ProductionLineDto savedProductionLine = productionLineService.startProduction(user.getTeam(),
                    request.getProductionLineId(),
                    request.getProductId(),
                    request.getAmount());
            StartProductionResponse response = new StartProductionResponse(savedProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }

    public void UpgradeProductionLineQuality(ProcessedRequest processedRequest, UpgradeProductionLineQualityRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineQuality(user.getTeam(),
                    request.getProductionLineId());
            UpgradeProductionLineQualityResponse response = new UpgradeProductionLineQualityResponse(savedProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (Exception e) {
            logger.debug(e);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }

    public void UpgradeProductionLineEfficiency(ProcessedRequest processedRequest, UpgradeProductionLineEfficiencyRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineEfficiency(user.getTeam(),
                    request.getProductionLineId());
            UpgradeProductionLineEfficiencyResponse response = new UpgradeProductionLineEfficiencyResponse(savedProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (Exception e) {
            logger.debug(e);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }
}