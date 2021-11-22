package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.WeeklyReportRepository;
import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;
import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class WeeklyReportService extends AbstractCrudService<WeeklyReportDto, WeeklyReport, Integer> {
    private final WeeklyReportRepository<WeeklyReport> weeklyReportRepository;

    public WeeklyReportService(WeeklyReportRepository<WeeklyReport> weeklyReportRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        setRepository(weeklyReportRepository);
    }
}
