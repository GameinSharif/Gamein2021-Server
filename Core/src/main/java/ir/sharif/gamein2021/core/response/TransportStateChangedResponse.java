package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class TransportStateChangedResponse extends ResponseObject implements Serializable {
    private TransportDto transportDto;
}
