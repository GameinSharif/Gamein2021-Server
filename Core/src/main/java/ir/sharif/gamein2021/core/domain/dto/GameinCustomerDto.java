package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameinCustomerDto implements BaseDto<Integer>
{
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
}
