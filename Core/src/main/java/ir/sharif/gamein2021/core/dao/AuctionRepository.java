package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Auction;
import ir.sharif.gamein2021.core.util.Enums.AuctionBidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    List<Auction> findAllByAuctionBidStatus(AuctionBidStatus auctionBidStatus);

    Auction findByFactoryId(Integer factoryId);

    Auction findAuctionByHighestBidTeamId(Integer teamId);
}
