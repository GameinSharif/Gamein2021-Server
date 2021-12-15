package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Auction.BidForAuctionRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Auction.BidForAuctionResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuctionController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final AuctionService auctionService;
    private final Gson gson;
    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;

    public void addBidForAuction(ProcessedRequest request, BidForAuctionRequest bidForAuctionRequest)
    {
        Integer teamId = request.teamId;
        TeamDto teamDto = teamService.loadById(teamId);
        Integer factoryId = bidForAuctionRequest.getFactoryId();

        BidForAuctionResponse response;
        try
        {
            auctionService.checkAuctionByTeam(teamId);

            AuctionDto auctionDto = auctionService.findAuctionByFactory(factoryId);
            auctionDto = auctionService.changeHighestBid(auctionDto, teamDto, bidForAuctionRequest.getRaiseAmount());

            AuctionDto responseAuction = AuctionDto.builder()
                    .highestBid(auctionDto.getHighestBid())
                    .factoryId(auctionDto.getFactoryId())
                    .highestBidTeamId(auctionDto.getHighestBidTeamId())
                    .lastRaiseAmount(auctionDto.getLastRaiseAmount())
                    .build();
            response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION, responseAuction, "success");
            pushMessageManager.sendMessageToAll(gson.toJson(response));
        }
        catch (EntityNotFoundException e)
        {
            //This is the first bid for this factory
            try
            {
                AuctionDto auctionDto = auctionService.bidForFirstTimeForThisFactory(teamDto, factoryId, bidForAuctionRequest.getRaiseAmount());
                AuctionDto responseAuction = AuctionDto.builder()
                        .highestBid(auctionDto.getHighestBid())
                        .factoryId(auctionDto.getFactoryId())
                        .highestBidTeamId(auctionDto.getHighestBidTeamId())
                        .lastRaiseAmount(auctionDto.getLastRaiseAmount())
                        .build();
                response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION, responseAuction, "success");
                pushMessageManager.sendMessageToAll(gson.toJson(response));
            }
            catch (Exception e2)
            {
                System.out.println(e2.getMessage());
                logger.debug(e2.getMessage());
                response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION, null, e2.getMessage());
                pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(response));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new BidForAuctionResponse(ResponseTypeConstant.BID_FOR_AUCTION, null, e.getMessage());
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(response));
        }
    }
}
