package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.WeeklyReportRepository;
import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;
import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeeklyReportService extends AbstractCrudService<WeeklyReportDto, WeeklyReport, Integer> {
    private final WeeklyReportRepository weeklyReportRepository;
    private final ModelMapper modelMapper;

    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository, ModelMapper modelMapper) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.modelMapper = modelMapper;
        setRepository(weeklyReportRepository);
    }

    public List<WeeklyReportDto> findAllByTeamId(Integer teamId) {
        return weeklyReportRepository.findAllByTeamId(teamId).stream()
                .map(x -> modelMapper.map(x, WeeklyReportDto.class)).collect(Collectors.toList());
    }
}
