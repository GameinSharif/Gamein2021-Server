package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer>
{
    List<Team> findAllByFactoryIdIsNullAndCountry(Country country);
    Team findTeamByFactoryId(Integer factoryId);
    List<Team> findAllByOrderByWealthDesc();
}
