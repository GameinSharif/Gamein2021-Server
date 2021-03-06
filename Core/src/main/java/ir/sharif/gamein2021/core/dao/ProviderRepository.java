package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Provider;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    List<Provider> findAllByTeamAndState(Team team, Enums.ProviderState state);
    List<Provider> findAllByTeamIsNotAndState(Team team, Enums.ProviderState state);

    @Override
    void deleteById(Integer integer);
}
