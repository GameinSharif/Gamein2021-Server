package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcDto {
    private Integer id;
    private Integer ownerId;
    private int buyingPrice;
    private int sellingPrice;
    private int startingWeak;
    private double latitude;
    private double longitude;
    private int capacity;
    private boolean isRawMaterial;
}
