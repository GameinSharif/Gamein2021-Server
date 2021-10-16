package ir.sharif.gamein2021.core.Service;


import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.Country;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer>
{

    private final TeamRepository repository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository repository, ModelMapper modelMapper)
    {
        this.repository = repository;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    @Transactional
    public List<TeamDto> findAllEmptyTeamWithCountry(Country country)
    {
        return repository.findAllByFactoryIdIsNullAndCountry(country).stream().
                map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }
}
