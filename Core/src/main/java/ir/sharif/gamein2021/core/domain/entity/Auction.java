package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.AuctionBidStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory_id", nullable = false, unique = true)
    private Integer factoryId;

    @Column(name = "highest_bid", nullable = false)
    private Integer highestBid;

    @OneToOne
    private Team highestBidTeam;

    @Column(name = "bids_count", nullable = false)
    private Integer bidsCount;

    @Column(name = "last_raise_amount", nullable = false)
    private Integer lastRaiseAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AuctionBidStatus auctionBidStatus;
}
