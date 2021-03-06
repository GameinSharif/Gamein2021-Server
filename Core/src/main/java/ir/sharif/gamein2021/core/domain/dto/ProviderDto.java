package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer teamId;
    private Integer productId;
    private Integer capacity;
    private Float price;
    private Enums.ProviderState state;
    private Integer storageId;
}
