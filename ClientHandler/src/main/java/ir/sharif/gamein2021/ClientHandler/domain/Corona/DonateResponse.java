package ir.sharif.gamein2021.ClientHandler.domain.Corona;

import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class DonateResponse extends ResponseObject implements Serializable
{
    private List<CoronaInfoDto> infos;
    private String result;

    public DonateResponse(ResponseTypeConstant responseTypeConstant, List<CoronaInfoDto> infos, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.infos = infos;
        this.result = result;
    }
}
