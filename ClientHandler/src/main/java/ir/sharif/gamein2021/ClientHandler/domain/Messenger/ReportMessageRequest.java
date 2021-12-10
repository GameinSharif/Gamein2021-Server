package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReportMessageRequest extends RequestObject implements Serializable
{
    private Integer chatId;
    private String messageText;
    private Integer reportedTeamId;
    private LocalDateTime insertedAt;
}
