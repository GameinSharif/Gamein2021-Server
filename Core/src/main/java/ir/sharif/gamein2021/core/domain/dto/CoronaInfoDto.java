package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoronaInfoDto {
    private Integer id;
    private Enums.Country country;
    private Float amountToBeCollect = 10000F;
    private Float currentCollectedAmount = 0F;
    private Boolean isCoronaOver = true;
}
