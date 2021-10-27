package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class GetAllActiveDcResponse extends ResponseObject implements Serializable {
    private List<DcDto> dcs;

    public GetAllActiveDcResponse(ResponseTypeConstant responseTypeConstant, List<DcDto> dcs) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.dcs = dcs;
    }
}
