package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team , Integer> {
    //Team findTeamById(Integer id);
}
