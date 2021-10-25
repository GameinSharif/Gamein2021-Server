package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Supplier;

import java.util.List;

public class ContractSupplierDto
{

    private Integer id;
    private Supplier supplier;
    private GameinCustomer gameinCustomer;
    private Integer productId;
    private Enums.ContractType contractType;
    private List<ContractSupplierDetail> contractSupplierDetails;
    private Integer terminatePenalty;

}
