package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.response.MoneyUpdateResponse;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class TeamManager
{
    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

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

    public void updateAllTeamsMoneyOnClient()
    {
        List<TeamDto> teams = teamService.list();
        for (TeamDto team : teams)
        {
            MoneyUpdateResponse moneyUpdateResponse = new MoneyUpdateResponse(
                    ResponseTypeConstant.MONEY_UPDATE,
                    team.getCredit(),
                    team.getWealth(),
                    team.getBrand(),
                    team.getDonatedAmount());

            pushMessageManager.sendMessageByTeamId(team.getId().toString(), gson.toJson(moneyUpdateResponse));
        }
    }
}
