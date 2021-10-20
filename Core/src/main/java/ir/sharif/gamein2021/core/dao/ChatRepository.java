package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer>
{
    Chat findByTeam1AndTeam2(Team team1, Team team2);
}
