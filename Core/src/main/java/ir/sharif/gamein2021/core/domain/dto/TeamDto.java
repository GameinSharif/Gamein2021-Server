package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto
{
    private Integer id;
    private String teamName;
    private Integer randomCountryIndex;
    private int shopXCoordinate , shopYCoordinate = 0;
}
