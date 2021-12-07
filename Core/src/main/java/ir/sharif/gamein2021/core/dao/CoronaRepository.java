package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.CoronaInfo;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoronaRepository extends JpaRepository<CoronaInfo, Integer> {
    public CoronaInfo findByCountry(Enums.Country country);
}
