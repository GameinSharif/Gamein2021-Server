package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.DynamicConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DynamicConfigRepository extends JpaRepository<DynamicConfig, String> {
}
