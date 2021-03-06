package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer>
{
    List<Offer> findOffersByTeamAndOfferStatus(Team team, OfferStatus offerStatus);
    List<Offer> findAllByTeamIsNotAndOfferStatusIs(Team team, OfferStatus offerStatus);
    List<Offer> findAllByAccepterTeamIs(Team team);
}
