package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcDto
{
    private Integer id;
    private Integer ownerId;
    private String name;
    private int buyingPrice;
    private int sellingPrice;
    private int startingWeek;
    private double latitude;
    private double longitude;
    private int capacity;
    private Enums.DCType type;
}
