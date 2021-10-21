package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.domain.dto.ChatDto;
import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetAllChatsResponse  extends ResponseObject implements Serializable {

    List<ChatDto> chatDtos;

    public GetAllChatsResponse(ResponseTypeConstant responseTypeConstant, List<ChatDto> chatDtos) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.chatDtos = chatDtos;
    }
}
