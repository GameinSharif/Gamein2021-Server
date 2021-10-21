package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.ConstructProductionLineRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.ConstructProductionLineResponse;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.GetProductionLinesRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.GetProductionLinesResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
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

    public void GetProductionLines(ProcessedRequest processedRequest, GetProductionLinesRequest getProductionLinesRequest) {
        int playerId = getProductionLinesRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = user.getTeam();

        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(userTeam);

        GetProductionLinesResponse response = new GetProductionLinesResponse(productionLines);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }

    public void constructProductionLine(ProcessedRequest processedRequest, ConstructProductionLineRequest constructProductionLineRequest) {
        int playerId = constructProductionLineRequest.playerId;
        UserDto user = userService.loadById(playerId);

        ProductionLineDto productionLine = new ProductionLineDto();

        productionLine.setStatus(Enums.ProductionLineStatus.ACTIVE);
        productionLine.setProductionLineTemplateId(constructProductionLineRequest.getProductionLineTemplateId());
        productionLine.setTeamId(user.getTeam().getId());
        productionLine.setEfficiencyLevel(0);
        productionLine.setQualityLevel(0);

        try {
            ProductionLineDto createdProductionLine = productionLineService.CreateProductionLine(productionLine);
            ConstructProductionLineResponse response = new ConstructProductionLineResponse(createdProductionLine);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
        } catch (InvalidProductionLineTemplateIdException e) {
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(e));
        }
    }
}
