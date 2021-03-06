package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractSupplierRepository extends JpaRepository<ContractSupplier, Integer>
{
    ContractSupplier findContractSupplierById(Integer id);
    List<ContractSupplier> findByTeamAndIsTerminatedFalse(Team team);
    List<ContractSupplier> findAllByContractDate(LocalDate date);
}
