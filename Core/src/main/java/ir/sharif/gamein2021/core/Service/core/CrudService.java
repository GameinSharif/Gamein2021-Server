package ir.sharif.gamein2021.core.Service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface CrudService<D, E, I extends Serializable> {
    List<D> list();

    Page<D> listWithPagination(Pageable pageable);

    D loadById(I id);

    D saveOrUpdate(D domainObject);

    List<D> saveAll(Iterable<E> all);

    void delete(I id);

    void deleteAll();
}
