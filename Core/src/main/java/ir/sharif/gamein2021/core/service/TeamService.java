package ir.sharif.gamein2021.core.service;


import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.exception.NotEnoughMoneyException;
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
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer>
{

    private final TeamRepository repository;
    private final ModelMapper modelMapper;
    private final CoronaService coronaService;

    public TeamService(TeamRepository repository,
                       ModelMapper modelMapper,
                       CoronaService coronaService)
    {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.coronaService = coronaService;
        setRepository(repository);
    }

    // TODO WHY ARE YOU RETURNING AN ENTIRE ENTITY!?
    @Transactional(readOnly = true)
    public Team findTeamById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(TeamNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findAllTeams()
    {
        List<TeamDto> teams = new ArrayList<>();
        for (TeamDto teamDto : list())
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
    public List<Team> findAllEmptyTeamWithCountry(Country country)
    {
        return repository.findAllByFactoryIdIsNullAndCountry(country);
    }

    @Transactional(readOnly = true)
    public Integer findTeamIdByFactoryId(Integer factoryId)
    {
        return repository.findTeamByFactoryId(factoryId).getId();
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getTeamsOrderByWealthDesc()
    {
        return repository.findAllByOrderByWealthDesc().stream()
                .map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findTeamsByFactoryIdIsNotNull()
    {
        return repository.findTeamsByFactoryIdIsNotNull().stream()
                .map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public List<CoronaInfoDto> donate(TeamDto team, Float donatedAmount)
    {
        team = loadById(team.getId());
        if (!coronaService.isCoronaStarted())
        {
            throw new InvalidRequestException();
        }
        if (donatedAmount > team.getCredit())
        {
            throw new NotEnoughMoneyException("You don't have this much money to donate!");
        }

        var coronaInfo = coronaService.findCoronaInfoWithCountry(team.getCountry());
        if (coronaInfo.getIsCoronaOver())
        {
            throw new InvalidRequestException("Corona is over in your country!");
        }

        var maximumMoneyToDonate = coronaService.calculateAvailableMoneyForDonate(team.getCountry());

        if (donatedAmount > maximumMoneyToDonate)
        {
            donatedAmount = maximumMoneyToDonate;
        }

        coronaService.addDonatedMoneyToCoronaInfo(donatedAmount, coronaInfo);

        team.setCredit(team.getCredit() - donatedAmount);
        team.setWealth(team.getWealth() - donatedAmount);
        team.setDonatedAmount(team.getDonatedAmount() + donatedAmount);
        saveOrUpdate(team);

        coronaService.changeAndSaveCoronaStatusForCountry(team.getCountry());

        return coronaService.getCoronasInfoIfCoronaIsStarted();
    }
}