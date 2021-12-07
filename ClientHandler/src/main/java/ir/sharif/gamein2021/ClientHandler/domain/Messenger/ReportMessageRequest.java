package ir.sharif.gamein2021.ClientHandler.domain.Messenger;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class ReportMessageRequest extends RequestObject implements Serializable {

    private String messageText;
    private Integer chatId;
    private Integer reportedTeamId;

}
