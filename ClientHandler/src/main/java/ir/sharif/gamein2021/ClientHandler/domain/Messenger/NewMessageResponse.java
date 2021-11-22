package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.domain.dto.ChatDto;
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
    private ChatDto chat;
    private MessageDto message;
    private String result;

    public NewMessageResponse(ResponseTypeConstant responseTypeConstant, ChatDto chat, MessageDto message, String result)
    {
        this.chat = chat;
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.message = message;
        this.result = result;
    }

}
