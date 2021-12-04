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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer> {

    private final TeamRepository repository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    // TODO WHY ARE YOU RETURNING AN ENTIRE ENTITY!?
    @Transactional(readOnly = true)
    public Team findTeamById(Integer id) {
        return getRepository().findById(id).orElseThrow(TeamNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findAllTeams()
    {
        List<TeamDto> teams = new ArrayList<>();
        for(TeamDto teamDto : list())
        {
            teams.add(TeamDto.builder()
            .id(teamDto.getId())
            .teamName(teamDto.getTeamName())
            .factoryId(teamDto.getFactoryId())
            .country(teamDto.getCountry())
            .build());
        }
        return teams;
    }

    @Transactional(readOnly = true)
    public List<Team> findAllEmptyTeamWithCountry(Country country) {
        return repository.findAllByFactoryIdIsNullAndCountry(country);
    }

    @Transactional(readOnly = true)
    public Integer findTeamIdByFactoryId(Integer factoryId) {
        return repository.findTeamByFactoryId(factoryId).getId();
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getTeamsOrderByWealthDesc() {
        return repository.findAllByOrderByWealthDesc().stream()
                .map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }

    public List<TeamDto> findTeamsByFactoryIdIsNotNull(){
        return repository.findTeamsByFactoryIdIsNotNull().stream()
                .map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }
}
