package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Provider;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    List<Provider> findAllByTeam(Team team);
    List<Provider> findAllByTeamIsNot(Team team);

    @Override
    void deleteById(Integer integer);
}
