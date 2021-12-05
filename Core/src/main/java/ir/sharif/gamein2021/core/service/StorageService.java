package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.StorageRepository;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.Storage;
import ir.sharif.gamein2021.core.domain.entity.StorageProduct;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
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


@Slf4j
@Service
public class StorageService extends AbstractCrudService<StorageDto, Storage, Integer>
{
    private final ModelMapper modelMapper;
    private final StorageRepository repository;
    private final StorageProductService storageProductService;
    private final TransportService transportService;
    private final DcService dcService;

    public StorageService(ModelMapper modelMapper, StorageRepository repository,
                          StorageProductService storageProductService,
                          @Lazy DcService dcService , TransportService transportService)
    {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.transportService = transportService;
        this.storageProductService = storageProductService;
        this.dcService = dcService;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public StorageDto loadById(Integer id)
    {
        Assert.notNull(id, "The id must not be null!");
        Storage storage = repository.findById(id).get();
        return repository.findById(id)
                .map(e -> modelMapper.map(e, StorageDto.class))
//                {
//                    StorageDto storageDto = modelMapper.map(e, StorageDto.class);
//                    List<StorageProductDto> products = e.getProducts().stream().
//                            map(t -> storageProductService.loadById(t.getId())).collect(Collectors.toList());
//                    storageDto.setProducts(products);
//                    return storageDto;
//                })
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Override
    public List<StorageDto> list()
    {
        return super.list();
    }

    @Transactional(readOnly = true)
    public List<StorageDto> findAllStorageForTeam(TeamDto teamDto)
    {
        List<StorageDto> storages = new ArrayList<>();
        storages.add(findStorageWithBuildingIdAndDc(teamDto.getFactoryId(), false));

        List<DcDto> dcs = dcService.getAllDcForTeam(teamDto);
        for (DcDto dcDto : dcs)
        {
            storages.add(findStorageWithBuildingIdAndDc(dcDto.getId(), true));
        }
        return storages;
    }

    @Transactional(readOnly = true)
    public boolean storageBelongsToTeam(Integer storageId, TeamDto teamDto) {
        List<StorageDto> allTeamStorage = findAllStorageForTeam(teamDto);
        for (StorageDto storageDto : allTeamStorage) {
            if (storageDto.getId().equals(storageId))
                return true;
        }
        return false;
    }

    @Transactional
    public StorageDto deleteProducts(Integer buildingId, boolean isDc, Integer productId, int amount)
    {
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        Product product = ReadJsonFilesManager.findProductById(productId);

        StorageProductDto storageProductDto = findProductStorageById(storage.getId(), product.getId());
        if (amount <= 0)
        {
            throw new InvalidRequestException("Amount should be positive bro.");
        }
        if (storageProductDto.getAmount() < amount)
        {
            throw new InvalidRequestException("You storage doesn't have this much products");
        }
        else
        {
            int index = storage.getProducts().indexOf(storageProductDto);
            storageProductDto.setAmount(storageProductDto.getAmount() - amount);
            storage.getProducts().set(index, storageProductDto);
            storageProductService.saveOrUpdate(storageProductDto);
            return loadById(storage.getId());
        }
    }

    //TODO need testing
    @Transactional
    public StorageDto emptyStorage(Integer buildingId, boolean isDc)
    {
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        for (StorageProductDto storageProductDto : storage.getProducts())
        {
            int index = storage.getProducts().indexOf(storageProductDto);
            storageProductDto.setAmount(0);
            storage.getProducts().set(index, storageProductDto);
            storageProductService.saveOrUpdate(storageProductDto);
        }
        return loadById(storage.getId());
    }

    @Override
    public StorageDto saveOrUpdate(StorageDto domainObject)
    {
        Assert.notNull(domainObject, "The domainObject must not be null!");
        Storage entity = modelMapper.map(domainObject, getEntityClass());
        entity.getProducts().clear();
        if (domainObject.getProducts() != null)
        {
            for (StorageProductDto s : domainObject.getProducts())
            {
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
    public Storage findStorageById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public StorageDto addProduct(Integer buildingId, boolean isDc, Integer productId, int amount)
    {
        Product product = ReadJsonFilesManager.findProductById(productId);
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        int availableCapacity = calculateAvailableCapacityInStorage(buildingId, isDc, product.getProductType());

        if (availableCapacity < amount * product.getVolumetricUnit())
        {
            amount = availableCapacity;
        }
        StorageProductDto storageProductDto = findProductStorageByIdNull(storage.getId(), product.getId());
        if (storageProductDto != null)
        {
            int index = storage.getProducts().indexOf(storageProductDto);
            storageProductDto.setAmount(storageProductDto.getAmount() + amount);
            storage.getProducts().set(index, storageProductDto);
            storageProductService.saveOrUpdate(storageProductDto);
        }
        else
        {
            storageProductDto = StorageProductDto.builder().productId(product.getId()).amount(amount).build();
            storage.getProducts().add(storageProductDto);
        }
        saveOrUpdate(storage);
        return loadById(storage.getId());
    }


    @Transactional(readOnly = true)
    public StorageProductDto findProductStorageById(Integer storageId, Integer productId)
    {
        StorageProductDto storageProduct = getStorageProductDto(storageId, productId);
        if (storageProduct != null)
        {
            return storageProduct;
        }
        throw new ProductNotFoundException("Product with id : " + productId + " does not exist in this storage!");
    }

    @Transactional(readOnly = true)
    public StorageProductDto findProductStorageByIdNull(Integer storageId, Integer productId)
    {
        return getStorageProductDto(storageId, productId);
    }

    private StorageProductDto getStorageProductDto(Integer storageId, Integer productId)
    {
        StorageDto storage = loadById(storageId);
        Product product = ReadJsonFilesManager.findProductById(productId);

        List<StorageProductDto> storageProductDtos = storage.getProducts();
        for (StorageProductDto storageProduct : storageProductDtos)
        {
            if (product.getId() == storageProduct.getProductId())
            {
                return storageProduct;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public StorageDto findStorageWithBuildingIdAndDc(Integer buildingId, boolean isDc)
    {
        Storage storage = repository.findByBuildingIdAndDc(buildingId, isDc);
        if (storage == null)
        {
            if (isDc)
            {
                throw new EntityNotFoundException("Dc with id " + buildingId + " does not exist");
            }
            else
            {
                throw new EntityNotFoundException("Factory with id " + buildingId + " does not exist");
            }
        }
        return loadById(storage.getId());
    }

    public StorageProductDto getStorageProductWithBuildingId(Integer buildingId, boolean isDc ,Integer productId){
        StorageDto storageDto = findStorageWithBuildingIdAndDc(buildingId , isDc );
        Assert.notNull(storageDto , "Storage with id "  + buildingId + "does not exist !");
        return getStorageProductDto(storageDto.getId() , productId);
    }


    @Transactional(readOnly = true)
    public int calculateAvailableCapacityInStorage(Integer buildingId, boolean isDc, Enums.ProductType productType)
    {
        StorageDto storage = findStorageWithBuildingIdAndDc(buildingId, isDc);
        int availableCapacity = 0;
        if (!isDc)
        {
            Factory factory = ReadJsonFilesManager.findFactoryById(buildingId);
            if (productType.equals(Enums.ProductType.RawMaterial))
            {
                availableCapacity = GameConstants.Instance.rawMaterialCapacity;
            }
            else if (productType.equals(Enums.ProductType.SemiFinished))
            {
                availableCapacity = GameConstants.Instance.semiFinishedProductCapacity;
            }
            else if (productType.equals(Enums.ProductType.Finished))
            {
                availableCapacity = GameConstants.Instance.finishedProductCapacity;
            }
            for (StorageProductDto storageProductDto : storage.getProducts())
            {
                Product storeProduct = ReadJsonFilesManager.findProductById(storageProductDto.getProductId());
                if (storeProduct.getProductType().equals(productType))
                {
                    availableCapacity -= storageProductDto.getAmount() * storeProduct.getVolumetricUnit();
                }
            }
        }
        else
        {
            DcDto dcDto = dcService.loadById(buildingId);
            availableCapacity = dcDto.getCapacity();
            if ((dcDto.getType() == Enums.DCType.SemiFinished && productType.equals(Enums.ProductType.SemiFinished)
                    || (dcDto.getType() == Enums.DCType.Finished && productType.equals(Enums.ProductType.Finished))))
            {
                for (StorageProductDto storageProductDto : storage.getProducts())
                {
                    Product storeProduct = ReadJsonFilesManager.findProductById(storageProductDto.getProductId());
                    if (storeProduct.getProductType().equals(productType))
                    {
                        availableCapacity -= storageProductDto.getAmount() * storeProduct.getVolumetricUnit();
                    }
                }
            }
            else
            {
                throw new InvalidRequestException("This products can't be add to this dc");
            }
            //TODO checking cheating exception
        }
        return availableCapacity;
    }

    @Transactional(readOnly = true)
    public int calculateTransportCapacity(Integer storageId , boolean isDc , Enums.ProductType productType){
        StorageDto storage = findStorageWithBuildingIdAndDc(storageId , isDc);
        int availableCapacityInStorage = calculateAvailableCapacityInStorage(storage.getBuildingId(),isDc , productType);
        List<TransportDto> transportDtos;
        if(isDc) {
            transportDtos = transportService.getTransportsByDestinationIdAndBuildingType(storageId, Enums.TransportNodeType.DC);
        }
        else {
            transportDtos = transportService.getTransportsByDestinationIdAndBuildingType(storageId, Enums.TransportNodeType.FACTORY);
        }
        for(TransportDto transport : transportDtos){
            Product product = ReadJsonFilesManager.findProductById(transport.getContentProductId());
            if(product.getProductType().equals(productType))
                availableCapacityInStorage -= product.getVolumetricUnit() * transport.getContentProductAmount();
        }
        return availableCapacityInStorage;
    }

}
