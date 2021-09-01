package ir.sharif.gamein2021.core.repository;

import ir.sharif.gamein2021.core.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository<TEntity extends BaseEntity, ID> extends JpaRepository<TEntity, ID> {
}
