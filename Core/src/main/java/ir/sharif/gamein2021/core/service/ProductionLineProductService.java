package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class ProductionLineProductService extends AbstractCrudService<ProductionLineProductDto, ProductionLineProduct, Integer> {
    private final ProductionLineProductRepository productionLineProductRepository;

    public ProductionLineProductService(ProductionLineProductRepository productionLineProductRepository) {
        this.productionLineProductRepository = productionLineProductRepository;
    }
}
