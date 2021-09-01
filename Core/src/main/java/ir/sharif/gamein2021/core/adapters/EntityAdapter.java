package ir.sharif.gamein2021.core.adapters;

import ir.sharif.gamein2021.core.entity.BaseEntity;
import ir.sharif.gamein2021.core.model.BaseModel;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface EntityAdapter<TEntity extends BaseEntity, TModel extends BaseModel> {
    TEntity convertToEntity(TModel model);
    TModel convertToModel(TEntity entity);
}
