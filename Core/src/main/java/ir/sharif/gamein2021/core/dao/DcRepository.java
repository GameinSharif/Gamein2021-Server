package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Dc;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcRepository extends JpaRepository<Dc, Integer>
{
    List<Dc> findAllByOwner(Team owner);
    List<Dc> findAllByStartingWeekIsLessThanEqual(int startingWeek);
}
