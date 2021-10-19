package ir.sharif.gamein2021.ClientHandler.domain.Auction;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;

import java.io.Serializable;

public class BidForAuctionResponse extends ResponseObject implements Serializable
{
    private AuctionDto auction;
    private String result;  //TODO change to enum

    public BidForAuctionResponse(ResponseTypeConstant responseTypeConstant, AuctionDto auction, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.auction = auction;
        this.result = result;
    }
}
