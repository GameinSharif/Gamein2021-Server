package ir.sharif.gamein2021.ClientHandler.domain.Auction;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class BidForAuctionRequest extends RequestObject implements Serializable
{
    private final Integer factoryId;
    private final Integer raiseAmount;
}
