package ir.sharif.gamein2021.core.dao;


import ir.sharif.gamein2021.core.domain.entity.Negotiation;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Integer> {
    List<Negotiation> findAllByDemanderOrSupplier(Team demander, Team supplier);
}
