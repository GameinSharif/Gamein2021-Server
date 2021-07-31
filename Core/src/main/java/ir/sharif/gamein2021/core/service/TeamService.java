package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.model.TeamModel;

import java.util.List;

public interface TeamService {
    TeamModel saveTeam(TeamModel team);
    boolean deleteTeam(final Long teamId);
    List<TeamModel> getAllTeams();
    TeamModel getTeamById(final Long teamId);
}
