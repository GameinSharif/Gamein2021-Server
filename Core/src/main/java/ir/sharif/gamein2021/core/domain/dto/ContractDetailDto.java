package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDetailDto implements BaseDto<Integer>
{
    private Integer id;
    private LocalDateTime contractDate;
    private Integer amount;
    private Integer pricePerUnit;
}
