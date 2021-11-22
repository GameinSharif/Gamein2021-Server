package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyReportRepository<T> extends JpaRepository<WeeklyReport, Integer> {
    List<T> findAllByTeamId(int teamId);
}
