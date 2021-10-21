package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionLineRepository extends JpaRepository<ProductionLine, Integer> {
}
