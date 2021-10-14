package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.model.Country;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDto {

    private Integer id;
    private Integer factoryId;
    private int higherPrice;
    private Integer HigherTeamId;
    private int numberOfOffers;
    private Country country;
}
