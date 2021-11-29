package ir.sharif.gamein2021.core.manager;


import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.NewsDto;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.response.SendNewsResponse;
import ir.sharif.gamein2021.core.service.NewsService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class NewsManager {
    private final PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final NewsService newsService;
    private final Gson gson = new Gson();

    public void SendNews(){
        Integer week = gameCalendar.getCurrentWeek();
        List<NewsDto> newsDtos = newsService.findByWeek(week);
        SendNewsResponse sendNewsResponse = new SendNewsResponse(ResponseTypeConstant.GET_NEWS, newsDtos);

        pushMessageManager.sendMessageToAll(gson.toJson(sendNewsResponse));
    }

}
