package ir.sharif.gamein2021.core.Service;


import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.model.Country;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer> {

    private final TeamRepository repository;
    private final ModelMapper modelMapper;


    public TeamService(TeamRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    @Transactional
    public TeamDto choiceRandomCountry(Integer id) {
        TeamDto teamDto = loadById(id);
        if (teamDto.getCountry() == null) {
            //TODO logic for random part
            int randomIndex = (int) (Math.random() * 7 + 1);
            Country country = Country.getCountryById(randomIndex);
            //TODO logic for random part
            teamDto.setCountry(country);
        }
        return saveOrUpdate(teamDto);
    }

}
