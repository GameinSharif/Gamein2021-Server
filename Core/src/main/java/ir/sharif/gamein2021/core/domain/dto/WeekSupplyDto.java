package ir.sharif.gamein2021.core.domain.dto;

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
    private Integer supplierId;
    private Integer productId;
    private Float price;
    private Float coefficient;
    private Integer sales;
}
