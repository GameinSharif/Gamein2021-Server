package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineIdException;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineTemplateIdException;
import ir.sharif.gamein2021.core.exception.ProductionLineMaximumEfficiencyLevelReachedException;
import ir.sharif.gamein2021.core.exception.ProductionLineMaximumQualityLevelReachedException;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Product;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionLineService extends AbstractCrudService<ProductionLineDto, ProductionLine, Integer> {
    private final ProductionLineRepository productionLineRepository;
    private final ProductionLineProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final GameCalendar gameCalendar;

    public ProductionLineService(ProductionLineRepository productionLineRepository,
                                 ProductionLineProductRepository productRepository,
                                 ModelMapper modelMapper, GameCalendar gameCalendar) {
        this.gameCalendar = gameCalendar;
        setRepository(productionLineRepository);
        this.productRepository = productRepository;
        this.productionLineRepository = productionLineRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductionLineDto> findProductionLinesByTeam(Team team) {
        List<ProductionLine> productionLines = productionLineRepository.findProductionLinesByTeam(team);
        return productionLines.stream()
                .map(x -> modelMapper.map(x, ProductionLineDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductionLineDto CreateProductionLine(ProductionLineDto productionLine) throws InvalidProductionLineTemplateIdException {
        Set<Integer> productLineTemplateIds = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).map(ProductionLineTemplate::getId).collect(Collectors.toSet());

        if (!productLineTemplateIds.contains(productionLine.getProductionLineTemplateId())) {
            throw new InvalidProductionLineTemplateIdException("Invalid productionLineTemplateId used for creating productionLine");
        }

        return saveOrUpdate(productionLine);
    }

    @Transactional
    public ProductionLineDto scrapProductionLine(Team team, Integer productionLineId) throws InvalidProductionLineIdException {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to scrap a production line with not active status");
        }

        // TODO check no product is in production.

        // TODO what to with scarp price. better to use e dictionary for saving productionLineTemplates.
        ProductionLineTemplate template = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).filter(x -> x.getId() == productionLine.getProductionLineTemplateId()).findFirst().orElse(null);
        int scrapPrice = template.getScrapPrice();

        productionLine.setStatus(Enums.ProductionLineStatus.SCRAPPED);
        ProductionLine savedProductionLine = productionLineRepository.saveAndFlush(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }

    @Transactional
    public ProductionLineDto startProduction(Team team, Integer productionLineId, Integer productId, Integer amount) throws InvalidProductionLineIdException {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to start a production on a scrapped production line.");
        }

        Product productTemplate = Arrays.stream(ReadJsonFilesManager.Products)
                .filter(x -> x.getId() == productId &&
                        x.getProductionLineTemplateId() == productionLine.getProductionLineTemplateId())
                .findFirst().orElse(null);

        if (productTemplate == null) {
            throw new InvalidProductionLineIdException("Selected productLine is not able to create selected product");
        }

        ProductionLineProduct inProductionProduct = productionLine.getProducts().stream()
                .filter(x -> gameCalendar.getCurrentDate().isAfter(x.getEndDate()))
                .findFirst().orElse(null);

        if (inProductionProduct != null) {
            throw new InvalidProductionLineIdException("ProductionLine is busy now.");
        }

        ProductionLineProduct newProduct = new ProductionLineProduct();
        newProduct.setProductId(productId);
        newProduct.setStartDate(gameCalendar.getCurrentDate().plusDays(1));
        newProduct.setAmount(amount);
//        newProduct.setEndDate();

        ProductionLineProduct savedProduct = productRepository.saveAndFlush(newProduct);
        return modelMapper.map(productionLineRepository.findById(productionLineId).orElse(null), ProductionLineDto.class);
        //TODO what to do?
    }

    @Transactional
    public ProductionLineDto upgradeProductionLineQuality(Team team, Integer productionLineId) throws Exception {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to upgrade a scrapped production line quality.");
        }

        Integer currentQualityLevel = productionLine.getQualityLevel();
        ProductionLineTemplate template = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).filter(x -> x.getId() == productionLine.getProductionLineTemplateId()).findFirst().orElse(null);

        if (currentQualityLevel + 1 >= template.getQualityLevels().size()) {
            throw new ProductionLineMaximumQualityLevelReachedException("Maximum quality level reached. Cannot upgrade quality level.");
        }

        productionLine.setQualityLevel(currentQualityLevel + 1);
        ProductionLine savedProductionLine = productionLineRepository.saveAndFlush(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }

    @Transactional
    public ProductionLineDto upgradeProductionLineEfficiency(Team team, Integer productionLineId) throws Exception {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to upgrade a scrapped production line efficiency.");
        }

        Integer currentEfficiencyLevel = productionLine.getEfficiencyLevel();
        ProductionLineTemplate template = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).filter(x -> x.getId() == productionLine.getProductionLineTemplateId()).findFirst().orElse(null);

        if (currentEfficiencyLevel + 1 >= template.getEfficiencyLevels().size()) {
            throw new ProductionLineMaximumEfficiencyLevelReachedException("Maximum efficiency level reached. Cannot upgrade efficiency level.");
        }

        productionLine.setEfficiencyLevel(currentEfficiencyLevel + 1);
        ProductionLine savedProductionLine = productionLineRepository.saveAndFlush(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }

    private ProductionLine getProductionLine(Team team, Integer productionLineId) throws InvalidProductionLineIdException {
        ProductionLine productionLine = productionLineRepository.findById(productionLineId).orElse(null);

        if (productionLine == null) {
            throw new InvalidProductionLineIdException("No production line founded with id = " + productionLineId);
        }

        if (!productionLine.getTeam().getId().equals(team.getId())) {
            throw new InvalidProductionLineIdException("Production line with id = " + productionLineId + " doesn't belong to team with id = " + team.getId());
        }

        return productionLine;
    }
}