package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer teamId;
    private Integer gameinCustomerId;
    private Integer productId;
    private Enums.ContractType contractType;
    private List<ContractDetailDto> contractDetails;
    private Integer terminatePenalty;
    private boolean isTerminated;
}
