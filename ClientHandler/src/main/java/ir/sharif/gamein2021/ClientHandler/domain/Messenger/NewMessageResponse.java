package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewMessageResponse extends ResponseObject implements Serializable
{
    private final MessageDto messageDto;
    private final String message;

    public NewMessageResponse(ResponseTypeConstant responseTypeConstant, MessageDto messageDto, String message)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.messageDto = messageDto;
        this.message = message;
    }

}
