package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.StorageRepository;
import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.domain.dto.StorageProductDto;
import ir.sharif.gamein2021.core.domain.entity.Storage;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService extends AbstractCrudService<StorageDto, Storage, Integer> {
    private final ModelMapper modelMapper;
    private final StorageRepository repository;
    private final StorageProductService storageProductService;

    public StorageService(ModelMapper modelMapper, StorageRepository repository, StorageProductService storageProductService) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.storageProductService = storageProductService;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public StorageDto loadById(Integer id) {
        Assert.notNull(id, "The id must not be null!");

        return repository.findById(id)
                .map(e -> {
                    StorageDto storageDto = modelMapper.map(e, getDtoClass());
                    List<StorageProductDto> products = e.getProducts().stream().
                            map(t -> storageProductService.loadById(t.getId())).collect(Collectors.toList());
                    storageDto.setProducts(products);
                    return storageDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Override
    public List<StorageDto> list() {
        return super.list();
    }

    @Transactional
    public StorageDto deleteProducts(Integer buildingId , boolean isDc , Integer productId , int amount){
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId , isDc);
        Product product = Product.findProductById(productId);

        StorageProductDto storageProductDto = findProductStorageById(storage.getId() , product.getId());
        if(storageProductDto.getAmount() < amount)
            throw new InvalidRequestException("You storage doesn't have this much products");
        else{
            storageProductDto.setAmount(storageProductDto.getAmount() - amount);
            storageProductService.saveOrUpdate(storageProductDto);
            return loadById(storage.getId());
        }
    }

    @Transactional(readOnly = true)
    public StorageProductDto findProductStorageById(Integer storageId , Integer productId){
        StorageDto storage = loadById(storageId);
        Product product = Product.findProductById(productId);

        List<StorageProductDto> storageProductDtos = storage.getProducts();
        for(StorageProductDto storageProduct : storageProductDtos){
            if(product.getId() == storageProduct.getProductId())
                return storageProduct;
        }
        throw new ProductNotFoundException("Product with id : " + productId + " does not exist in this dc!");
    }

    @Transactional(readOnly = true)
    public StorageDto findStorageWithBuildingIdAndDc(Integer buildingId, boolean isDc){
         Storage storage = repository.findByBuildingIdAndDc(buildingId , isDc);
         if(storage == null){
             if(isDc){
                 throw new EntityNotFoundException("Dc with id "  + buildingId + " does not exist");
             }else
                 throw new EntityNotFoundException("Factory with id "  + buildingId + " does not exist");
         }
         return loadById(storage.getId());
    }

}
