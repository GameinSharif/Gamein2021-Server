package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetAllChatsResponse  extends ResponseObject implements Serializable {

    List<ChatDto> chats;

    public GetAllChatsResponse(ResponseTypeConstant responseTypeConstant, List<ChatDto> chats) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.chats = chats;
    }
}
