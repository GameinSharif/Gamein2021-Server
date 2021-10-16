package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.domain.BidForAuctionRequest;
import ir.sharif.gamein2021.ClientHandler.domain.BidForAuctionResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.Service.AuctionService;
import ir.sharif.gamein2021.core.Service.TeamService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuctionController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final AuctionService auctionService;
    private final Gson gson;
    private final PushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;

    public void addBidForAuction(ProcessedRequest request, BidForAuctionRequest bidForAuctionRequest) {
        BidForAuctionResponse response;
        try {
            Integer id = bidForAuctionRequest.playerId;
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            Integer factoryId = bidForAuctionRequest.getFactoryId();
            AuctionDto auctionDto = auctionService.findAuctionByFactory(factoryId);
            int highestPrice = auctionDto.getHighestBid();
            //TODO this is only for testing
            auctionService.changeHigherTeam(auctionDto , teamDto , highestPrice +100 );
            auctionDto = auctionService.loadById(auctionDto.getId());
            AuctionDto responseAuction = AuctionDto.builder().highestBid(auctionDto.getHighestBid())
                    .factoryId(auctionDto.getFactoryId()).highestBidTeam(auctionDto.getHighestBidTeam())
                    .build();
            response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION
                    ,responseAuction , "Bid has added successfully!");
        }catch (Exception e){
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION , null , e.getMessage());
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
    }
}
