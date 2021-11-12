package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionLineProductService extends AbstractCrudService<ProductionLineProductDto, ProductionLineProduct, Integer> {
    private final ProductionLineProductRepository productionLineProductRepository;
    private final ProductionLineRepository productionLineRepository;
    private final StorageService storageService;

    public ProductionLineProductService(ProductionLineProductRepository productionLineProductRepository,
                                        ProductionLineRepository productionLineRepository,
                                        StorageService storageService) {
        this.productionLineProductRepository = productionLineProductRepository;
        this.productionLineRepository = productionLineRepository;
        this.storageService = storageService;
    }

    public void finishProductCreation(LocalDate currentDate) {
        List<ProductionLineProduct> products = productionLineProductRepository.findProductionLineProductsByEndDateEquals(currentDate);
        Set<Integer> productLineIds = products.stream().map(ProductionLineProduct::getProductionLineId).collect(Collectors.toSet());
        Map<Integer, ProductionLine> productionLineHashMap = productionLineRepository.findAllById(productLineIds).stream().collect(Collectors.toMap(ProductionLine::getId, x -> x));

        for (ProductionLineProduct product : products) {
            ProductionLine productionLine = productionLineHashMap.getOrDefault(product.getProductionLineId(), null);

            if (productionLine == null) {
                continue;
            }

            storageService.addProduct(productionLine.getTeam().getFactoryId(), false, product.getProductId(), product.getAmount());
        }
    }
}
