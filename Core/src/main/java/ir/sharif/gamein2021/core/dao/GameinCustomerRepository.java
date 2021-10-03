package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameinCustomerRepository extends JpaRepository<GameinCustomer, Integer>
{

}
