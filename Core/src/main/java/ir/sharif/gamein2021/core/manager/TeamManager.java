package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.GameConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class TeamManager
{
    private final TeamService teamService;

    private float checkBrandBoundary(float brand)
    {
        if (brand < GameConstants.brandMin)
        {
            return GameConstants.brandMin;
        }
        else if (brand > GameConstants.brandMax)
        {
            return GameConstants.brandMax;
        }
        return brand;
    }

    public void updateTeamsBrands(float amount)
    {
        List<TeamDto> teamDtos = teamService.list();
        for (TeamDto teamDto : teamDtos)
        {
            updateTeamBrand(teamDto, amount);
        }
    }

    public void updateTeamBrand(TeamDto teamDto, float amount)
    {
        float newBrand = teamDto.getBrand() + amount;
        teamDto.setBrand(checkBrandBoundary(newBrand));
        teamService.saveOrUpdate(teamDto);
    }
}
