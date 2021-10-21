package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;

public class ProductionLineService extends AbstractCrudService<ProductionLineDto, ProductionLine, Integer> {
    private final ProductionLineRepository productionLineRepository;

    public ProductionLineService(ProductionLineRepository productionLineRepository) {
        this.productionLineRepository = productionLineRepository;
    }
}
