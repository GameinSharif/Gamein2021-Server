package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ReportMessageResponse extends ResponseObject implements Serializable
{
    private final MessageDto message;
    private final String result;

    public ReportMessageResponse(ResponseTypeConstant responseTypeConstant, String result, MessageDto message)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.result = result;
        this.message = message;
    }
}
