package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.StorageRepository;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.StorageDto;
import ir.sharif.gamein2021.core.domain.dto.StorageProductDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Storage;
import ir.sharif.gamein2021.core.domain.entity.StorageProduct;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Factory;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class StorageService extends AbstractCrudService<StorageDto, Storage, Integer> {
    private final ModelMapper modelMapper;
    private final StorageRepository repository;
    private final StorageProductService storageProductService;
    private final DcService dcService;

    public StorageService(ModelMapper modelMapper, StorageRepository repository,
                          StorageProductService storageProductService, @Lazy DcService dcService) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.storageProductService = storageProductService;
        this.dcService = dcService;
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

    @Transactional(readOnly = true)
    public List<StorageDto> findAllStorageForTeam(TeamDto teamDto) {
        List<DcDto> dcs = dcService.getAllDcForTeam(teamDto);
        List<StorageDto> storages = new ArrayList<>();
        for (DcDto dcDto : dcs) {
            storages.add(findStorageWithBuildingIdAndDc(dcDto.getId(), true));
        }
        storages.add(findStorageWithBuildingIdAndDc(teamDto.getFactoryId(), false));
        return storages;
    }

    @Transactional
    public StorageDto deleteProducts(Integer buildingId, boolean isDc, Integer productId, int amount) {
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        Product product = Product.findProductById(productId);

        StorageProductDto storageProductDto = findProductStorageById(storage.getId(), product.getId());
        if (storageProductDto.getAmount() < amount)
            throw new InvalidRequestException("You storage doesn't have this much products");
        else {
            int index = storage.getProducts().indexOf(storageProductDto);
            storageProductDto.setAmount(storageProductDto.getAmount() - amount);
            storage.getProducts().set(index , storageProductDto);
            storageProductService.saveOrUpdate(storageProductDto);
            return loadById(storage.getId());
        }
    }

    @Override
    public StorageDto saveOrUpdate(StorageDto domainObject) {
        Assert.notNull(domainObject, "The domainObject must not be null!");
        Storage entity = modelMapper.map(domainObject, getEntityClass());
        entity.getProducts().clear();
        if (domainObject.getProducts() != null) {
            for (StorageProductDto s : domainObject.getProducts()) {
                StorageProductDto product = storageProductService.saveOrUpdate(s);
                StorageProduct productEntity = storageProductService.findStorageProductById(product.getId());
                entity.getProducts().add(productEntity);
            }
        }
        Storage result = repository.save(entity);
        log.debug("save/update entity {}", result);
        return modelMapper.map(result, getDtoClass());
    }

    @Transactional(readOnly = true)
    public Storage findStorageById(Integer id) {
        return getRepository().findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public StorageDto addProduct(Integer buildingId, boolean isDc, Integer productId, int amount) {
        Product product = Product.findProductById(productId);
        StorageProductDto storageProductDto = null;
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        int availableCapacity = calculateAvailableCapacity(buildingId, isDc, product.getProductType());
        if (availableCapacity < amount * product.getVolumetricUnit())
            throw new InvalidRequestException("Product out of boundary exception, You only have " + availableCapacity + " to add to this building!");
        storageProductDto = findProductStorageByIdNull(storage.getId(), product.getId());
        if(storageProductDto != null) {
            int index = storage.getProducts().indexOf(storageProductDto);
            storageProductDto.setAmount(storageProductDto.getAmount() + amount);
            storage.getProducts().set(index , storageProductDto);
            storageProductService.saveOrUpdate(storageProductDto);
        }
        else {
            storageProductDto = StorageProductDto.builder().productId(product.getId()).amount(amount).build();
            storage.getProducts().add(storageProductDto);
        }
        saveOrUpdate(storage);
        return loadById(storage.getId());
    }

    @Transactional(readOnly = true)
    public StorageProductDto findProductStorageById(Integer storageId, Integer productId) {
        StorageDto storage = loadById(storageId);
        Product product = Product.findProductById(productId);

        List<StorageProductDto> storageProductDtos = storage.getProducts();
        for (StorageProductDto storageProduct : storageProductDtos) {
            if (product.getId() == storageProduct.getProductId())
                return storageProduct;
        }
        throw new ProductNotFoundException("Product with id : " + productId + " does not exist in this dc!");
    }

    @Transactional(readOnly = true)
    public StorageProductDto findProductStorageByIdNull(Integer storageId, Integer productId) {
        StorageDto storage = loadById(storageId);
        Product product = Product.findProductById(productId);

        List<StorageProductDto> storageProductDtos = storage.getProducts();
        for (StorageProductDto storageProduct : storageProductDtos) {
            if (product.getId() == storageProduct.getProductId())
                return storageProduct;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public StorageDto findStorageWithBuildingIdAndDc(Integer buildingId, boolean isDc) {
        Storage storage = repository.findByBuildingIdAndDc(buildingId, isDc);
        if (storage == null) {
            if (isDc) {
                throw new EntityNotFoundException("Dc with id " + buildingId + " does not exist");
            } else
                throw new EntityNotFoundException("Factory with id " + buildingId + " does not exist");
        }
        return loadById(storage.getId());
    }


    @Transactional(readOnly = true)
    public int calculateAvailableCapacity(Integer buildingId, boolean isDc, Enums.ProductType productType) {
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        int availableCapacity = 0;
        if (!isDc) {
            Factory factory = Factory.findFactoryById(buildingId);
            if (productType.equals(Enums.ProductType.RawMaterial))
                availableCapacity = factory.getRawMaterialCapacity();
            else if (productType.equals(Enums.ProductType.SemiFinished))
                availableCapacity = factory.getSecondaryMaterialCapacity();
            else if (productType.equals(Enums.ProductType.Finished))
                availableCapacity = factory.getFinalMaterialCapacity();
            for (StorageProductDto storageProductDto : storage.getProducts()) {
                Product storeProduct = Product.findProductById(storageProductDto.getProductId());
                if (storeProduct.getProductType().equals(productType)) {
                    availableCapacity -= storageProductDto.getAmount() * storeProduct.getVolumetricUnit();
                }
            }
        } else {
            DcDto dcDto = dcService.loadById(buildingId);
            availableCapacity = dcDto.getCapacity();
            if ((dcDto.isRawMaterial() && productType.equals(Enums.ProductType.RawMaterial)
                    || (!dcDto.isRawMaterial() && productType.equals(Enums.ProductType.SemiFinished)))) {
                for (StorageProductDto storageProductDto : storage.getProducts()) {
                    Product storeProduct = Product.findProductById(storageProductDto.getProductId());
                    if (storeProduct.getProductType().equals(productType)) {
                        availableCapacity -= storageProductDto.getAmount() * storeProduct.getVolumetricUnit();
                    }
                }
            } else {
                throw new InvalidRequestException("This products can't be add to this dc");
            }
            //TODO checking cheating exception
        }
        return availableCapacity;
    }

}
