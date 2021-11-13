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
import ir.sharif.gamein2021.core.service.TeamService;
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
    private final TeamService teamService;
    private final ProductionLineService productionLineService;

    private final Gson gson = new Gson();

    public ProductionLineController(LocalPushMessageManager pushMessageManager,
                                    TeamService teamService, UserService userService,
                                    ProductionLineService productionLineService) {
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.teamService = teamService;
        this.productionLineService = productionLineService;
    }

    public void GetProductionLines(ProcessedRequest processedRequest, GetProductionLinesRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(userTeam);

        GetProductionLinesResponse response = new GetProductionLinesResponse(productionLines);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }

    public void constructProductionLine(ProcessedRequest processedRequest, ConstructProductionLineRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        ProductionLineDto productionLine = new ProductionLineDto();

        productionLine.setStatus(Enums.ProductionLineStatus.IN_CONSTRUCTION);
        productionLine.setProductionLineTemplateId(request.getProductionLineTemplateId());
        productionLine.setTeamId(user.getTeamId());
        productionLine.setEfficiencyLevel(0);
        productionLine.setQualityLevel(0);

        ConstructProductionLineResponse response = new ConstructProductionLineResponse();

        try {
            ProductionLineDto createdProductionLine = productionLineService.CreateProductionLine(productionLine);
            response.setProductionLine(createdProductionLine);
        } catch (InvalidProductionLineTemplateIdException e) {
            logger.debug(e);
        } finally {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        }
    }

    public void scrapProductionLine(ProcessedRequest processedRequest, ScrapProductionLineRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        ScrapProductionLineResponse response = new ScrapProductionLineResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.scrapProductionLine(teamService.findTeamById(user.getTeamId()), request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
        } finally {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        }
    }

    public void StartProduction(ProcessedRequest processedRequest, StartProductionRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        StartProductionResponse response = new StartProductionResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.startProduction(teamService.findTeamById(user.getTeamId()),
                    request.getProductionLineId(),
                    request.getProductId(),
                    request.getAmount());
            response.setProductionLine(savedProductionLine);
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
        } finally {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        }
    }

    public void UpgradeProductionLineQuality(ProcessedRequest processedRequest, UpgradeProductionLineQualityRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        UpgradeProductionLineQualityResponse response = new UpgradeProductionLineQualityResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineQuality(teamService.findTeamById(user.getTeamId()),
                    request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        }
    }

    public void UpgradeProductionLineEfficiency(ProcessedRequest processedRequest, UpgradeProductionLineEfficiencyRequest request) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);

        UpgradeProductionLineEfficiencyResponse response = new UpgradeProductionLineEfficiencyResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineEfficiency(teamService.findTeamById(user.getTeamId()),
                    request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        }
    }
}