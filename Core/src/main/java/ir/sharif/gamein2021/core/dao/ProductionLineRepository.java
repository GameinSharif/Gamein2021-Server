package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductionLineRepository extends JpaRepository<ProductionLine, Integer> {
    List<ProductionLine> findProductionLinesByTeam(Team team);
    List<ProductionLine> findProductionLinesByTeamAndStatus(Team team, Enums.ProductionLineStatus productionLineStatus);
    List<ProductionLine> findProductionLinesByStatusEqualsAndActivationDateLessThanEqual(Enums.ProductionLineStatus status, LocalDate activationDate);
    List<ProductionLine> findProductionLinesByStatusEquals(Enums.ProductionLineStatus status);
}
