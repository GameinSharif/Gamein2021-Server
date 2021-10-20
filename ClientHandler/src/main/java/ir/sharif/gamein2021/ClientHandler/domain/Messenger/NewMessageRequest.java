package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NewMessageRequest extends RequestObject implements Serializable
{
    private Integer receiverTeamId;
    private String text;
    private LocalDateTime insertedAt;
}
