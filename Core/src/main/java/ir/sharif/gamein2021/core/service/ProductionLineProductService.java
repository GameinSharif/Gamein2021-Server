package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ProductCreationCompletedRequest;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
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
    private final ClientHandlerRequestSenderInterface clientHandlerRequestSender;

    public ProductionLineProductService(ProductionLineProductRepository productionLineProductRepository,
                                        ProductionLineRepository productionLineRepository,
                                        StorageService storageService,
                                        ClientHandlerRequestSenderInterface clientHandlerRequestSender) {
        this.productionLineProductRepository = productionLineProductRepository;
        this.productionLineRepository = productionLineRepository;
        this.storageService = storageService;
        this.clientHandlerRequestSender = clientHandlerRequestSender;
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

            ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.get(productionLine.getProductionLineTemplateId());
            int amount = (template.getEfficiencyLevels().get(productionLine.getEfficiencyLevel()).getEfficiencyPercentage() / 100) * product.getAmount();

            storageService.addProduct(productionLine.getTeam().getFactoryId(), false, product.getProductId(), amount);
            clientHandlerRequestSender.send(new ProductCreationCompletedRequest(productionLine, product, "Done"));
        }
    }
}
