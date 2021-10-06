package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer>
{
    User findUserByUsernameAndPassword(String username, String password);
}
