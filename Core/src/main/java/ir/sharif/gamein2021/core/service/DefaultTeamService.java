package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.entity.Team;
import ir.sharif.gamein2021.core.model.TeamModel;
import ir.sharif.gamein2021.core.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("defaultTeamService")
public class DefaultTeamService implements TeamService {

    private final TeamRepository teamRepository;

    public DefaultTeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public TeamModel saveTeam(TeamModel team) {
        Team teamModel = populateTeamEntity(team);
        return populateTeamModel(teamRepository.save(teamModel));
    }

    @Override
    public boolean deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
        return true;
    }

    @Override
    public List<TeamModel> getAllTeams() {
        List<TeamModel> teams = new ArrayList<>();
        List<Team> teamList = teamRepository.findAll();
        teamList.forEach(team -> {
            teams.add(populateTeamModel(team));
        });
        return teams;
    }

    @Override
    public TeamModel getTeamById(Long teamId) {
        return populateTeamModel(teamRepository.findById(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found")));
    }

    private TeamModel populateTeamModel(final Team team) {
        TeamModel teamData = new TeamModel();
        teamData.setId(team.getId());
        teamData.setName(team.getTeamName());
        return teamData;
    }

    private Team populateTeamEntity(TeamModel teamData) {
        Team team = new Team();
        team.setTeamName(teamData.getName());
        return team;
    }
}
