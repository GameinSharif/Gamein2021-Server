package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.response.CoronaInfoResponse;
import ir.sharif.gamein2021.core.service.CoronaService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CoronaManager {
    private final PushMessageManagerInterface pushMessageManager;
    private final Gson gson = new Gson();
    private final CoronaService coronaService;

    public void SendCoronaInfoToAllUsers() {
        List<CoronaInfoDto> coronaInfos = coronaService.getCoronasInfoIfCoronaIsStarted();

        CoronaInfoResponse response = new CoronaInfoResponse(ResponseTypeConstant.CORONA_INFO, coronaInfos);
        pushMessageManager.sendMessageToAll(gson.toJson(response));
    }
}
