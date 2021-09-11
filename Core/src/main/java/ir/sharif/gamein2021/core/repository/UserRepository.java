package ir.sharif.gamein2021.core.repository;

import ir.sharif.gamein2021.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
