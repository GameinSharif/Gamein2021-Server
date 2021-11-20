package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TeamManager;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ProductCreationCompletedRequest;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductionLineProductService extends AbstractCrudService<ProductionLineProductDto, ProductionLineProduct, Integer> {
    private final ProductionLineProductRepository productionLineProductRepository;
    private final ProductionLineRepository productionLineRepository;
    private final StorageService storageService;
    private final ClientHandlerRequestSenderInterface clientHandlerRequestSender;
    private final TeamService teamService;
    private final TeamManager teamManager;

    public void finishProductCreation(LocalDate currentDate) {
        List<ProductionLineProduct> products = productionLineProductRepository.findProductionLineProductsByEndDateEquals(currentDate);
        Set<Integer> productLineIds = products.stream().map(ProductionLineProduct::getProductionLineId).collect(Collectors.toSet());
        Map<Integer, ProductionLine> productionLineHashMap = productionLineRepository.findAllById(productLineIds).stream().collect(Collectors.toMap(ProductionLine::getId, x -> x));

        for (ProductionLineProduct product : products) {
            ProductionLine productionLine = productionLineHashMap.getOrDefault(product.getProductionLineId(), null);

            if (productionLine == null) {
                continue;
            }

            //TODO efficiency level
            Integer amount = product.getAmount();

            // Upgrading brand when production ends.
            ProductionLineTemplate productionLineTemplate = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);
            Float brandCoefficient = (float)(productionLineTemplate.getQualityLevels().get(productionLine.getQualityLevel()).getBrandIncreaseRatioPerProduct());
            teamManager.updateTeamBrand(teamService.loadById(productionLine.getTeam().getId()),  amount * brandCoefficient);

            storageService.addProduct(productionLine.getTeam().getFactoryId(), false, product.getProductId(), amount);
            clientHandlerRequestSender.send(new ProductCreationCompletedRequest(productionLine, product, "Done"));
        }
    }
}
