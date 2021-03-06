package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductionLineProductRepository extends JpaRepository<ProductionLineProduct, Integer> {
    boolean existsProductionLineProductByProductionLineIdEqualsAndEndDateGreaterThanEqual(Integer productionLineId, LocalDate endDate);
    List<ProductionLineProduct> findProductionLineProductsByEndDateEquals(LocalDate endDate);
}
