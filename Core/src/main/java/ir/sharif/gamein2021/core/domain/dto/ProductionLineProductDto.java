package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLineProductDto {
    private Integer id;
    private Integer productId;
    private Integer productionLineId;
    private Integer amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
