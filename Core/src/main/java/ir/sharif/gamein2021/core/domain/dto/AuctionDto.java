package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.AuctionBidStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDto
{
    private Integer id;
    private Integer factoryId;
    private int highestBid;
    private Integer highestBidTeam;
    private int bidsCount;
    private AuctionBidStatus auctionBidStatus;
}
