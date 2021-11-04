package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContractSupplierRepository extends JpaRepository<ContractSupplier, Integer>
{
    ContractSupplier findByContractSupplierDetail(ContractSupplierDetail contractSupplierDetail);
}
