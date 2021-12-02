package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.CoronaCoefficient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoronaCoefficientRepository extends JpaRepository<CoronaCoefficient, Integer> {

    CoronaCoefficient findByWeek(Integer week);
}
