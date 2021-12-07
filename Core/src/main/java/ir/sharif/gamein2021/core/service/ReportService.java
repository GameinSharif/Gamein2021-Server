package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.OfferRepository;
import ir.sharif.gamein2021.core.dao.ReportRepository;
import ir.sharif.gamein2021.core.domain.dto.ReportDto;
import ir.sharif.gamein2021.core.domain.entity.Report;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService extends AbstractCrudService<ReportDto, Report, Integer> {

    private final ReportRepository reportRepository;
    private final TeamService teamService;

    public ReportService(ReportRepository reportRepository, ModelMapper modelMapper, TeamService teamService)
    {
        this.reportRepository = reportRepository;
        this.teamService = teamService;
        setRepository(reportRepository);
    }

    public List<Report> getReports(String messageText, Integer reporterTeamId, Integer reportedTeamId) {
        return reportRepository.findAllByMessageTextAndReporterTeamAndReportedTeam(
                messageText,
                teamService.findTeamById(reporterTeamId),
                teamService.findTeamById(reportedTeamId)
        );
    }

}
