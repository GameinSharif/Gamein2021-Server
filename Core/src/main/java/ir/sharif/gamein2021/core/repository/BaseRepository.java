package ir.sharif.gamein2021.core.repository;

import ir.sharif.gamein2021.core.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<TEntity extends BaseEntity> extends JpaRepository<TEntity, Integer> {
}
