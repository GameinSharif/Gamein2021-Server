package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ChatDto implements BaseDto<Integer> {

    private Integer id;
    private LocalDateTime latestMessageDate;
    private Integer team1Id;
    private Integer team2Id;
    private List<MessageDto> messageDtos;

    @Override
    public String toString() {
        return "ChatDto{" +
                "id=" + id +
                ", latestMessageDate=" + latestMessageDate +
                ", team1=" + team1Id +
                ", team2=" + team2Id +
                ", messageDtos=" + messageDtos +
                '}';
    }
}
