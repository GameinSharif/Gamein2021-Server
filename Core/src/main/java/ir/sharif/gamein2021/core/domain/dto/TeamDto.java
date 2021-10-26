package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.Country;
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
    private Country country;
    private Integer factoryId;
    private int credit;
}
