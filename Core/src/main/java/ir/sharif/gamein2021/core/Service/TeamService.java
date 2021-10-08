package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.TeamNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService extends AbstractCrudService<TeamDto, Team, Integer> {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository teamRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        setRepository(teamRepository);
    }

    @Transactional(readOnly = true)
    public Team findById(Integer id) {
        return getRepository().findById(id).orElseThrow(TeamNotFoundException::new);
    }


}
