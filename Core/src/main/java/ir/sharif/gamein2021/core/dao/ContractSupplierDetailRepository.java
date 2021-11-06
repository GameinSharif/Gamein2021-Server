package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.domain.entity.WeekSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractSupplierDetailRepository extends JpaRepository<ContractSupplierDetail, Integer>
{
    List<ContractSupplierDetail> findAllByContractDate(LocalDate date);
}
