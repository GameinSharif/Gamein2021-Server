package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.*;

import java.time.LocalDate;

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
    private Float credit;
    private Float brand;
    private Float wealth;
    private Float inFlow;
    private Float outFlow;
    private Float transportationCost;
    private Float productionCost;
    private Float donatedAmount;
    private Long usedWater;
    private LocalDate banEnd;
}
