package ir.sharif.gamein2021.core.manager;


import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@AllArgsConstructor
@Component
public class TeamManager {
    private final TeamService teamService;

    private float checkBrandBoundary(float brand){
        float min = 1;
        float max = 100;
        if(brand < min) return min;
        else if(brand > max) return max;
        return brand;
    }
    public void updateTeamsBrands(float amount){
        List<TeamDto> teamDtos = teamService.getAllTeams();
        for(TeamDto teamDto:teamDtos){
            float newBrand = teamDto.getBrand() + amount;
            teamDto.setBrand(checkBrandBoundary(newBrand));
            teamService.saveOrUpdate(teamDto);
        }
    }
    public void updateTeamBrand(TeamDto teamDto, float amount){
        float newBrand = teamDto.getBrand() + amount;
        teamDto.setBrand(checkBrandBoundary(newBrand));
        teamService.saveOrUpdate(teamDto);
    }
}
