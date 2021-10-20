package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;

import java.io.Serializable;
import java.util.List;

public class GetAllAuctionsResponse extends ResponseObject implements Serializable
{
    private List<AuctionDto> auctions;

    public GetAllAuctionsResponse(ResponseTypeConstant responseTypeConstant, List<AuctionDto> auctions)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.auctions = auctions;
    }
}
