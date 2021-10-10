package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findAllByWinnerTeamIsNull();
}
