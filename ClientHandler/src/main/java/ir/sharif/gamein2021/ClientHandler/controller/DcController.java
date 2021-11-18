package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.BuyingDcRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.BuyingDcResponse;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.SellingDcRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.SellingDcResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DcController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final DcService dcService;
    private final Gson gson;
    private final LocalPushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;

    public void buyDc(ProcessedRequest request, BuyingDcRequest buyingDcRequest) {
        Integer id = request.playerId;
        BuyingDcResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            DcDto dc = dcService.loadById(buyingDcRequest.getDcId());

            dc = dcService.buyDc(dc, teamDto);
            response = new BuyingDcResponse(ResponseTypeConstant.BUY_DC, dc, "success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new BuyingDcResponse(ResponseTypeConstant.BUY_DC, null, e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }
    public void sellDc(ProcessedRequest request, SellingDcRequest sellingDcRequest) {
        Integer id = request.playerId;
        SellingDcResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            DcDto dc = dcService.loadById(sellingDcRequest.getDcId());

            dc = dcService.sellDc(dc, teamDto);
            response = new SellingDcResponse(ResponseTypeConstant.SELL_DC, dc, "success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new SellingDcResponse(ResponseTypeConstant.SELL_DC, null, e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }
}
