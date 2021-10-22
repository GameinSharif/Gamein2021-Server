package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Dc;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DcRepository extends JpaRepository<Dc , Integer> {
    public List<Dc> findAllByOwner(Team owner);
}
