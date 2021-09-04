package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.core.entity.Team;
import ir.sharif.gamein2021.core.repository.BaseRepository;
import ir.sharif.gamein2021.core.repository.TeamRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class TeamController
{
    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    public int getTeamId(String username, String password) throws Exception
    {
        //TODO this part has bugs
        Team team = new Team();
        Example<Team> teamExample = Example.of(team);
        Optional<Team> optionalTeam = teamRepository.findOne(teamExample);
        if (optionalTeam.isPresent())
        {
            return optionalTeam.get().getId();
        }
        throw new Exception("Wrong Username or Password");
    }
}
