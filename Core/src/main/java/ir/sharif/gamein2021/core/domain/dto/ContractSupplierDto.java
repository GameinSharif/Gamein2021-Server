package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractSupplierDto
{

    private Integer id;
    private Integer supplierId;
    private Integer teamId;
    private Integer materialId;
    private Enums.ContractType contractType;
    private List<ContractSupplierDetailDto> contractSupplierDetails;
    private Integer terminatePenalty;
    private Boolean isTerminated;

}
