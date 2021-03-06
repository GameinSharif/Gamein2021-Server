package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.WeekSupply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeekSupplyRepository extends JpaRepository<WeekSupply, Integer>
{
    List<WeekSupply> findAllByWeek(Integer week);
    WeekSupply findAllBySupplierIdAndProductIdAndWeek(Integer supplierId, Integer productId, Integer week);
}
