package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findAllByFactoryIdIsNullAndCountry(Country country);
}
