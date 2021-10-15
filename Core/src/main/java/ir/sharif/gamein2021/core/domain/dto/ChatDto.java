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
    private List<Integer> teamIds;
    private ArrayList<MessageDto> messageDtos;

}
