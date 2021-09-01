package ir.sharif.gamein2021.core.service;

import java.util.List;

public interface BaseServiceInterface<TEntity, TModel> {
    TModel save(TModel model);
    boolean delete(final Integer entityId);
    List<TModel> getAll();
    TModel getEntityById(final Integer entityId);
}
