package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.WeekDemandRepository;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.domain.entity.WeekDemand;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeekDemandService extends AbstractCrudService<WeekDemandDto, WeekDemand, Integer>
{
    private final WeekDemandRepository weekDemandRepository;
    private final ModelMapper modelMapper;

    public WeekDemandService(WeekDemandRepository weekDemandRepository, ModelMapper modelMapper)
    {
        this.weekDemandRepository = weekDemandRepository;
        this.modelMapper = modelMapper;
        setRepository(weekDemandRepository);
    }

    @Transactional(readOnly = true)
    public List<WeekDemandDto> findByWeek(Integer week)
    {
        List<WeekDemand> weekDemands = weekDemandRepository.findWeekDemandsByWeek(week);
        return weekDemands.stream()
                .map(e -> modelMapper.map(e, WeekDemandDto.class))
                .collect(Collectors.toList());
    }
}
