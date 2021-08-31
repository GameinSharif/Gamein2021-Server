package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.core.entity.Team;
import ir.sharif.gamein2021.core.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TeamController
{
    @Autowired
    TeamRepository teamRepository;

    public int getTeamId(String username, String password) throws Exception
    {
        //TODO this part has bugs
        Team team = new Team(username, password);
        Example<Team> teamExample = Example.of(team);
        Optional<Team> optionalTeam = teamRepository.findOne(teamExample);
        if (optionalTeam.isPresent())
        {
            return optionalTeam.get().getId();
        }
        throw new Exception("Wrong Username or Password");
    }
}
