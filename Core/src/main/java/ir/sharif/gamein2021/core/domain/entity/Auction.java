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

    @Column(name = "highest_bid")
    private int highestBid;

    @OneToOne
    private Team highestBidTeam;

    @Column(name = "bids_count")
    private int bidsCount;

    @Enumerated(value = EnumType.STRING)
    private AuctionBidStatus auctionBidStatus;
}
