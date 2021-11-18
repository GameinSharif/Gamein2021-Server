package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Contract;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>
{
    List<Contract> findContractsByTeam(Team team);
    List<Contract> findContractsByContractDate(LocalDate contractDate);
}
