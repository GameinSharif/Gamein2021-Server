package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Integer> {
    Storage findByBuildingIdAndDc(Integer buildingId, boolean dc);
}
