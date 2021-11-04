package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekSupplyDto
{
    private Integer id;
    private Integer week;
    private Supplier supplier;
    private Integer productId;
    private Integer price;
    private Integer sales;
}
