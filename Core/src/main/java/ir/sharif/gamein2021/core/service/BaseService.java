package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.adapters.EntityAdapter;
import ir.sharif.gamein2021.core.entity.BaseEntity;
import ir.sharif.gamein2021.core.model.BaseModel;
import ir.sharif.gamein2021.core.repository.BaseRepository;
import ir.sharif.gamein2021.core.service.exceptions.NotFoundEntityException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BaseService<TEntity extends BaseEntity, TModel extends BaseModel>
        implements BaseServiceInterface<TEntity, TModel> {
    private final BaseRepository<TEntity, Integer> repository;
    private final EntityAdapter<TEntity, TModel> adapter;

    public BaseService(BaseRepository<TEntity, Integer> repository, EntityAdapter<TEntity, TModel> adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    @Override
    public TModel save(TModel model) {
        TEntity entity = adapter.convertToEntity(model);
        return adapter.convertToModel(repository.save(entity));
    }

    @Override
    public boolean delete(Integer entityId) {
        repository.deleteById(entityId);
        return true;
    }

    @Override
    public List<TModel> getAll() {
        List<TModel> models = new ArrayList<>();
        List<TEntity> teamList = repository.findAll();
        teamList.forEach(team -> {
            models.add(adapter.convertToModel(team));
        });
        return models;
    }

    @Override
    public TModel getEntityById(Integer entityId) {
        return adapter.convertToModel(repository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
    }

    @Override
    public TModel findOne(Example<TEntity> entityExample) throws NotFoundEntityException {
        Optional<TEntity> optionalEntity = repository.findOne(entityExample);
        if (optionalEntity.isPresent())
        {
            return adapter.convertToModel(optionalEntity.get());
        }

        throw new EntityNotFoundException("Wrong Username or Password");
    }
}
