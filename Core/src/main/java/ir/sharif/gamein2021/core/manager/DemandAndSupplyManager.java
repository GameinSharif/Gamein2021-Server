package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.response.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.core.response.GetCurrentWeekSuppliesResponse;
import ir.sharif.gamein2021.core.service.WeekDemandService;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class DemandAndSupplyManager
{
    private final WeekDemandService weekDemandService;
    private final WeekSupplyService weekSupplyService;
    private final PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void SendCurrentWeekSupplyAndDemandsToAllUsers()
    {
        int week = gameCalendar.getWeek();

        List<WeekDemandDto> currentWeekDemands = weekDemandService.findByWeek(week);
        GetCurrentWeekDemandsResponse getCurrentWeekDemandsResponse = new GetCurrentWeekDemandsResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_DEMANDS,
                currentWeekDemands
                );

        List<WeekSupplyDto> currentWeekSupplies = weekSupplyService.findByWeek(week);
        GetCurrentWeekSuppliesResponse getCurrentWeekSuppliesResponse = new GetCurrentWeekSuppliesResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_SUPPLIES,
                currentWeekSupplies
        );

        pushMessageManager.sendMessageToAll(gson.toJson(getCurrentWeekDemandsResponse));
        pushMessageManager.sendMessageToAll(gson.toJson(getCurrentWeekSuppliesResponse));
    }
}
