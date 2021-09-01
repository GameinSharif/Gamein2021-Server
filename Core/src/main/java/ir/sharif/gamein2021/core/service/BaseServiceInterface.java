package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.exceptions.NotFoundEntityException;
import org.springframework.data.domain.Example;

import java.util.List;

public interface BaseServiceInterface<TEntity, TModel> {
    TModel save(TModel model);
    boolean delete(final Integer entityId);
    List<TModel> getAll();
    TModel getEntityById(final Integer entityId);
    TModel findOne(Example<TEntity> entityExample) throws NotFoundEntityException;
}
