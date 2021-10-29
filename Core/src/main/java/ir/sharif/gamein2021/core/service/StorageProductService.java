package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.StorageProductRepository;
import ir.sharif.gamein2021.core.domain.dto.StorageProductDto;
import ir.sharif.gamein2021.core.domain.entity.StorageProduct;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class StorageProductService extends AbstractCrudService<StorageProductDto, StorageProduct, Integer> {
    private final ModelMapper modelMapper;
    private final StorageProductRepository repository;

    public StorageProductService(ModelMapper modelMapper, StorageProductRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public StorageProductDto loadById(Integer id) {
        Assert.notNull(id, "The id must not be null!");

        return repository.findById(id)
                .map(e -> modelMapper.map(e, getDtoClass()))
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Override
    public List<StorageProductDto> list() {
        return super.list();
    }


}
