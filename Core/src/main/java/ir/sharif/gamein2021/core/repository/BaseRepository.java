package ir.sharif.gamein2021.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<TEntity, ID> extends JpaRepository<TEntity, ID> {
}
