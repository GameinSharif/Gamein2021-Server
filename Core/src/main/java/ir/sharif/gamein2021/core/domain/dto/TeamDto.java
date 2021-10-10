package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.model.Country;
import ir.sharif.gamein2021.core.domain.model.Factory;
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
}
