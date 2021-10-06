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
    private Team team;
    private GameinCustomer gameinCustomer;
    private Integer productId;
    private Enums.ContractType contractType;
    private List<ContractDetail> contractDetails;
    private Integer terminatePenalty;
}
