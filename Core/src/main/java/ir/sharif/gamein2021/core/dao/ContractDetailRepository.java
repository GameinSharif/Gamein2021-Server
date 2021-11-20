package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractDetailRepository extends JpaRepository<ContractDetail, Integer>
{
    List<ContractDetail> findAllByContractDate(LocalDate date);
}
