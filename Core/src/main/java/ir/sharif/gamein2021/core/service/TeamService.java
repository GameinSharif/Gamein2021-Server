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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer> {

    private final TeamRepository repository;
    private final ModelMapper modelMapper;
    private final CoronaService coronaService;

    public TeamService(TeamRepository repository,
                       ModelMapper modelMapper,
                       CoronaService coronaService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.coronaService = coronaService;
        setRepository(repository);
    }

    // TODO WHY ARE YOU RETURNING AN ENTIRE ENTITY!?
    @Transactional(readOnly = true)
    public Team findTeamById(Integer id) {
        return getRepository().findById(id).orElseThrow(TeamNotFoundException::new);
    }

    @Transactional
    public List<TeamDto> findAllEmptyTeamWithCountry(Country country) {
        return repository.findAllByFactoryIdIsNullAndCountry(country).stream().
                map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }

    public Integer findTeamIdByFactoryId(Integer factoryId) {
        return repository.findTeamByFactoryId(factoryId).getId();
    }

    public List<TeamDto> getTeamsOrderByWealthDesc() {
        return repository.findAllByOrderByWealthDesc().stream()
                .map(e -> modelMapper.map(e, TeamDto.class)).collect(Collectors.toList());
    }
    @Transactional
    public List<CoronaInfoDto> donate(TeamDto team , Float donatedAmount){
        team = loadById(team.getId());
        if(!coronaService.isCoronaStarted())
            throw new InvalidRequestException();
        if(donatedAmount < team.getCredit())
            throw new NotEnoughMoneyException("You don't have this much money to donate!");
        var coronaInfo = coronaService.findCoronaInfoWithCountry(team.getCountry());
        if(coronaInfo.isCoronaOver())
            throw new InvalidRequestException("Corona is over in your country!");
        var maximumMoneyToDonate = coronaService.calculateAvailableMoneyForDonate(team.getCountry());
        donatedAmount = checkMaximumAvailableAmountToDonate(donatedAmount, maximumMoneyToDonate);
        reduceTeamCredit(team, donatedAmount);
        addDonatedMoneyToCoronaInfo(donatedAmount, coronaInfo);
        addDonatedMoneyToTeam(team, donatedAmount);
        saveOrUpdate(team);
        coronaService.changeAndSaveCoronaStatusForCountry(team.getCountry());
        return coronaService.getCoronasInfoIfCoronaIsStarted();
    }

    private void addDonatedMoneyToTeam(TeamDto team, Float donatedAmount) {
        team.setDonatedAmount(team.getDonatedAmount() + donatedAmount);
    }

    private void addDonatedMoneyToCoronaInfo(Float donatedAmount, CoronaInfoDto coronaInfo) {
        coronaInfo.setCurrentCollectedAmount(coronaInfo.getCurrentCollectedAmount() + donatedAmount);
    }

    private void reduceTeamCredit(TeamDto team, Float donatedAmount) {
        team.setCredit(team.getCredit() - donatedAmount);
    }

    private Float checkMaximumAvailableAmountToDonate(Float donatedAmount, Float maximumMoneyToDonate) {
        if(donatedAmount > maximumMoneyToDonate)
            donatedAmount = maximumMoneyToDonate;
        return donatedAmount;
    }
}
