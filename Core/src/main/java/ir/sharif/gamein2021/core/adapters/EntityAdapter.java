package ir.sharif.gamein2021.core.adapters;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
public interface EntityAdapter<TEntity, TModel> {
    TEntity convertToEntity(TModel model);
    TModel convertToModel(TEntity entity);
}
