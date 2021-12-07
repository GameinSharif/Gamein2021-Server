package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Auction;
import ir.sharif.gamein2021.core.domain.entity.Report;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByMessageTextAndReporterTeamAndReportedTeam(String messageText, Team reporterTeam, Team reportedTeam);
}
