package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Contract;
import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>
{
    List<Contract> findContractsByTeamAndIsTerminatedIsFalse(Team team);
    List<Contract> findContractsByContractDate(LocalDate contractDate);
    Contract findContractByTeamAndGameinCustomerAndProductIdAndContractDate(Team team, GameinCustomer gameinCustomer, int productId, LocalDate contractDate);
    List<Contract> findContractsByGameinCustomerAndProductIdAndContractDateAndIsTerminatedIsFalse(GameinCustomer gameinCustomer, int productId, LocalDate contractDate);
}
