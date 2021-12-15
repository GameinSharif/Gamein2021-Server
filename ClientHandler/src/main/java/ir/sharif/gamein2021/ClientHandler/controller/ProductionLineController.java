package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineIdException;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineTemplateIdException;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.service.ProductionLineService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ProductionLineController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;
    private final ProductionLineService productionLineService;
    private final Gson gson = new Gson();

    public void GetProductionLines(ProcessedRequest processedRequest, GetProductionLinesRequest request) {
        Team userTeam = teamService.findTeamById(processedRequest.teamId);

        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(userTeam);

        GetProductionLinesResponse response = new GetProductionLinesResponse(productionLines);
        pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
    }

    public void constructProductionLine(ProcessedRequest processedRequest, ConstructProductionLineRequest request) {
        ProductionLineDto productionLine = new ProductionLineDto();

        productionLine.setStatus(Enums.ProductionLineStatus.IN_CONSTRUCTION);
        productionLine.setProductionLineTemplateId(request.getProductionLineTemplateId());
        productionLine.setTeamId(processedRequest.teamId);
        productionLine.setEfficiencyLevel(0);
        productionLine.setQualityLevel(0);

        ConstructProductionLineResponse response = new ConstructProductionLineResponse();

        try {
            ProductionLineDto createdProductionLine = productionLineService.CreateProductionLine(productionLine);
            response.setProductionLine(createdProductionLine);
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(response));
        } catch (InvalidProductionLineTemplateIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
        }
    }

    public void scrapProductionLine(ProcessedRequest processedRequest, ScrapProductionLineRequest request) {
        ScrapProductionLineResponse response = new ScrapProductionLineResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.scrapProductionLine(teamService.findTeamById(processedRequest.teamId), request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(response));
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
        }
    }

    public void StartProduction(ProcessedRequest processedRequest, StartProductionRequest request) {
        StartProductionResponse response = new StartProductionResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.startProduction(teamService.findTeamById(processedRequest.teamId),
                    request.getProductionLineId(),
                    request.getProductId(),
                    request.getAmount());
            response.setProductionLine(savedProductionLine);
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(response));
        } catch (InvalidProductionLineIdException e) {
            logger.debug(e);
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
        }
    }

    public void UpgradeProductionLineQuality(ProcessedRequest processedRequest, UpgradeProductionLineQualityRequest request) {
        UpgradeProductionLineQualityResponse response = new UpgradeProductionLineQualityResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineQuality(teamService.findTeamById(processedRequest.teamId), request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(response));
        } catch (Exception e) {
            logger.debug(e);
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
        }
    }

    public void UpgradeProductionLineEfficiency(ProcessedRequest processedRequest, UpgradeProductionLineEfficiencyRequest request) {
        UpgradeProductionLineEfficiencyResponse response = new UpgradeProductionLineEfficiencyResponse();

        try {
            ProductionLineDto savedProductionLine = productionLineService.upgradeProductionLineEfficiency(teamService.findTeamById(processedRequest.teamId), request.getProductionLineId());
            response.setProductionLine(savedProductionLine);
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(response));
        } catch (Exception e) {
            logger.debug(e);
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(response));
        }
    }
}