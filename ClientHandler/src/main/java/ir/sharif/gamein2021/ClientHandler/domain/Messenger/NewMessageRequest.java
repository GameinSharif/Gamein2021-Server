package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.MessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class NewMessageRequest extends RequestObject implements Serializable {
    MessageDto messageDto;
}
