package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDto {
    private Integer id;  //--> null
    private Integer factoryId;
    private int winnerPrice;
    private Integer winnerTeam;
    private Integer numberOfOffers;
}
