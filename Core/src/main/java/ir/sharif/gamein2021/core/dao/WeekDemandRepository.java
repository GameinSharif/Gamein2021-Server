package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.WeekDemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekDemandRepository extends JpaRepository<WeekDemand, Integer>
{
    List<WeekDemand> findWeekDemandsByWeek(Integer week);
}
