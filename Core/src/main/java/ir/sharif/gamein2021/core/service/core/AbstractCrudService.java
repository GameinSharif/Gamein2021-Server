package ir.sharif.gamein2021.core.service.core;

import ir.sharif.gamein2021.core.domain.entity.Chat;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class AbstractCrudService<D, E, I extends Serializable> implements CrudService<D, E, I> {

    protected Class<E> entityClass;
    protected Class<D> dtoClass;
    private JpaRepository<E, I> jpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    protected JpaRepository<E, I> getRepository() {
        return jpaRepository;
    }

    public void setRepository(JpaRepository<E, I> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> list() {
        return jpaRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, getDtoClass()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> listWithPagination(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(e -> modelMapper.map(e, getDtoClass()));
    }

    @Override
    @Transactional(readOnly = true)
    public D loadById(I id) {
        assertIdNotNull(id);
        return jpaRepository.findById(id)
                .map(e -> modelMapper.map(e, getDtoClass()))
                .orElseThrow(() -> new EntityNotFoundException("can not find entity "+ getEntityClass().getSimpleName() + "by id: " + id +" "));
    }

    @Override
    @Transactional
    public D saveOrUpdate(D domainObject) {
        Assert.notNull(domainObject, "The domainObject must not be null!");
        E entity = modelMapper.map(domainObject, getEntityClass());
        E result = jpaRepository.save(entity);
        log.debug("save/update entity {}", result);
        return modelMapper.map(result, getDtoClass());
    }

    @Override
    @Transactional
    public List<D> saveAll(Iterable<E> all) {
        Assert.notNull(all, "The Iterable entity must not be null!");
        return jpaRepository.saveAll(all)
                .stream()
                .map(e -> modelMapper.map(e, getDtoClass()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(I id) {
        assertIdNotNull(id);
        jpaRepository.deleteById(id);
        log.debug("delete entity by id: {}", id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
        log.debug("delete all {}", getEntityClass().getSimpleName());
    }

    private void assertIdNotNull(I id) {
        Assert.notNull(id, "The id must not be null!");
    }

    @SuppressWarnings("unchecked")
    public Class<D> getDtoClass() {
        if (dtoClass == null) {
            Type type = getClass().getGenericSuperclass();
            while (type.equals(AbstractCrudService.class)) {
                type = getClass().getGenericSuperclass();
            }
            ParameterizedType paramType = (ParameterizedType) type;
            dtoClass = (Class<D>) paramType.getActualTypeArguments()[0];
        }
        return dtoClass;
    }

    @SuppressWarnings("unchecked")
    public Class<E> getEntityClass() {
        if (entityClass == null) {
            Type type = getClass().getGenericSuperclass();
            while (type.equals(AbstractCrudService.class)) {
                type = getClass().getGenericSuperclass();
            }
            ParameterizedType paramType = (ParameterizedType) type;
            entityClass = (Class<E>) paramType.getActualTypeArguments()[1];
        }
        return entityClass;
    }
}
