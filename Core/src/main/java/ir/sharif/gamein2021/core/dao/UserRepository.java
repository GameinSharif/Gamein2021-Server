package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Player, Integer>
{
    Player findUserByUsernameAndPassword(String username, String password);
    Player findUserById(Integer id);
}
