package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.response.GameTimeResponse;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GameDateManager
{
    private final PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void SendGameDateToAllUsers()
    {
        GameTimeResponse gameTimeResponse = new GameTimeResponse(ResponseTypeConstant.GAME_TIME,
                gameCalendar.getCurrentDate());

        pushMessageManager.sendMessageToAll(gson.toJson(gameTimeResponse));
    }

    public void SendGameDateToThisUser(Integer userId)
    {
        GameTimeResponse gameTimeResponse = new GameTimeResponse(ResponseTypeConstant.GAME_TIME,
                gameCalendar.getCurrentDate());

        pushMessageManager.sendMessageByUserId(userId.toString(), gson.toJson(gameTimeResponse));
    }
}
