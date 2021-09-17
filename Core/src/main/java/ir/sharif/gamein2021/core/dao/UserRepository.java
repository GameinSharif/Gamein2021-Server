package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Integer> {
}