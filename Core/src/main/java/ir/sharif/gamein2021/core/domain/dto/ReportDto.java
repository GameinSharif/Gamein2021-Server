package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {

    private Integer id;
    private Integer chatId;
    private String messageText;
    private Integer reporterTeamId;
    private Integer reportedTeamId;
    private LocalDateTime reportedAt;
    private LocalDateTime sentAt;

}
