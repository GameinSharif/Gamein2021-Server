package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.domain.entity.Provider;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer>
{
    List<Offer> findOffersByTeam(Team team);
    List<Offer> findAllByTeamIsNot(Team team);
}
