package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CoronaInfoResponse extends ResponseObject implements Serializable {
    List<CoronaInfoDto> coronaInfos;

    public CoronaInfoResponse(ResponseTypeConstant responseTypeConstant , List<CoronaInfoDto> coronaInfos) {
        this.coronaInfos = coronaInfos;
        this.responseTypeConstant = responseTypeConstant.ordinal();
    }
}
