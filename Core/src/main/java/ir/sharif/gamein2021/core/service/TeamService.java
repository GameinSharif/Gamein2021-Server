package ir.sharif.gamein2021.core.service;


import ir.sharif.gamein2021.core.exception.TeamNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
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

    // TODO WHY ARE YOU RETURNING AN ENTIRE ENTITY!?
    @Transactional(readOnly = true)
    public Team findTeamById(Integer id){
        return getRepository().findById(id).orElseThrow(TeamNotFoundException::new);
    }

    @Transactional
    public List<TeamDto> findAllEmptyTeamWithCountry(Country country)
    {
        return repository.findAllByFactoryIdIsNullAndCountry(country).stream().
                map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }


    public Integer findTeamIdByFactoryId(Integer factoryId) {
        return repository.findTeamByFactoryId(factoryId).getId();
    }

    public List<TeamDto> getAllTeams(){
        return repository.findAll().stream().
                map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }
}
