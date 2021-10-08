package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}