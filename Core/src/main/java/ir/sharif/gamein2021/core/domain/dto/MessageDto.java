package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto implements BaseDto<Integer> {

    private Integer id;
    private Integer chatId;
    private Integer senderTeamId;
    private Integer receiverTeamId;
    private String text;
    private LocalDateTime dateTime;

}
