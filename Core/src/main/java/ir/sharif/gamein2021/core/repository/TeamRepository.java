package ir.sharif.gamein2021.core.repository;

import ir.sharif.gamein2021.core.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
