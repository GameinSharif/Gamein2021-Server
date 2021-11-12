package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
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
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductionLineService extends AbstractCrudService<ProductionLineDto, ProductionLine, Integer> {
    private final ProductionLineRepository productionLineRepository;
    private final ProductionLineProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final GameCalendar gameCalendar;
    private final TeamRepository teamRepository;

    public ProductionLineService(ProductionLineRepository productionLineRepository,
                                 ProductionLineProductRepository productRepository,
                                 ModelMapper modelMapper,
                                 GameCalendar gameCalendar,
                                 TeamRepository teamRepository) {
        setRepository(productionLineRepository);
        this.teamRepository = teamRepository;
        this.gameCalendar = gameCalendar;
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
        ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);

        if (template == null) {
            throw new InvalidProductionLineTemplateIdException("Invalid productionLineTemplateId used for creating productionLine");
        }

        Team team = teamRepository.getById(productionLine.getTeamId());
        if (team.getCredit() - template.getConstructionCost() < 0) {
            throw new InvalidProductionLineTemplateIdException("Invalid Operation. You don't have enough money to construct production line.");
        }

        team.setCredit(team.getCredit() - template.getConstructionCost());

        teamRepository.saveAndFlush(team);
        return saveOrUpdate(productionLine);
    }

    @Transactional
    public ProductionLineDto scrapProductionLine(Team team, Integer productionLineId) throws InvalidProductionLineIdException {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to scrap a production line with not active status");
        }

        if (productRepository.existsProductionLineProductByProductionLineIdEqualsAndEndDateGreaterThanEqual(productionLineId, gameCalendar.getCurrentDate())) {
            throw new InvalidProductionLineIdException("Production line is busy now and cannot be scrapped");
        }

        ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);
        Team teamObject = teamRepository.getById(team.getId());
        int scrapPrice = template.getScrapPrice();
        teamObject.setCredit(teamObject.getCredit() + scrapPrice);
        teamRepository.saveAndFlush(teamObject);

        productionLine.setStatus(Enums.ProductionLineStatus.SCRAPPED);
        ProductionLine savedProductionLine = productionLineRepository.saveAndFlush(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }

    @Transactional
    public ProductionLineDto startProduction(Team team, Integer productionLineId, Integer productId, Integer amount) throws InvalidProductionLineIdException {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to start a production on a deactivated production line.");
        }

        ProductionLineTemplate productLineTemplate = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);

        if (productLineTemplate == null) {
            throw new InvalidProductionLineIdException("Selected productLine is not able to create selected product");
        }

        LocalDate currentDate = gameCalendar.getCurrentDate();

        ProductionLineProduct inProductionProduct = productionLine.getProducts().stream()
                .filter(x -> currentDate.isBefore(x.getEndDate()))
                .findFirst().orElse(null);

        if (inProductionProduct != null) {
            throw new InvalidProductionLineIdException("ProductionLine is busy now.");
        }

        float newCredit = team.getCredit() - productLineTemplate.getSetupCost() - amount * productLineTemplate.getProductionCostPerOneProduct();
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to produce new production.");
        }
        team.setCredit(newCredit);
        teamRepository.saveAndFlush(team);

        int productionDuration = (amount / productLineTemplate.getDailyProductionRate()) + 1;

        ProductionLineProduct newProduct = new ProductionLineProduct();
        newProduct.setProductId(productId);
        newProduct.setStartDate(currentDate.plusDays(1));
        newProduct.setAmount(amount);
        newProduct.setEndDate(currentDate.plusDays(1 + productionDuration));
        newProduct.setProductionLineId(productionLineId);

        ProductionLineProduct savedProduct = productRepository.saveAndFlush(newProduct);
        productionLine.getProducts().add(savedProduct);
        return modelMapper.map(productionLine, ProductionLineDto.class);
        //TODO what to do?
    }

    @Transactional
    public ProductionLineDto upgradeProductionLineQuality(Team team, Integer productionLineId) throws Exception {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to upgrade a deactivated production line quality.");
        }

        Integer currentQualityLevel = productionLine.getQualityLevel();
        ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);

        if (currentQualityLevel + 1 >= template.getQualityLevels().size()) {
            throw new ProductionLineMaximumQualityLevelReachedException("Maximum quality level reached. Cannot upgrade quality level.");
        }

        float newCredit = team.getCredit() - template.getQualityLevels().get(currentQualityLevel + 1).getUpgradeCost();
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to upgrade quality level.");
        }
        team.setCredit(newCredit);
        teamRepository.saveAndFlush(team);

        productionLine.setQualityLevel(currentQualityLevel + 1);
        ProductionLine savedProductionLine = productionLineRepository.saveAndFlush(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }

    @Transactional
    public ProductionLineDto upgradeProductionLineEfficiency(Team team, Integer productionLineId) throws Exception {
        ProductionLine productionLine = getProductionLine(team, productionLineId);

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to upgrade a deactivated production line efficiency.");
        }

        Integer currentEfficiencyLevel = productionLine.getEfficiencyLevel();
        ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.getOrDefault(productionLine.getProductionLineTemplateId(), null);

        if (currentEfficiencyLevel + 1 >= template.getEfficiencyLevels().size()) {
            throw new ProductionLineMaximumEfficiencyLevelReachedException("Maximum efficiency level reached. Cannot upgrade efficiency level.");
        }

        float newCredit = team.getCredit() - template.getEfficiencyLevels().get(currentEfficiencyLevel + 1).getUpgradeCost();
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to upgrade efficiency level.");
        }
        team.setCredit(newCredit);
        teamRepository.saveAndFlush(team);

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

    @Transactional
    public void enableProductionLines() {
        List<ProductionLine> productionLines = productionLineRepository.findProductionLinesByStatusEqualsAndActivationDateLessThanEqual(Enums.ProductionLineStatus.IN_CONSTRUCTION, gameCalendar.getCurrentDate());
        for (ProductionLine productionLine : productionLines) {
            productionLine.setStatus(Enums.ProductionLineStatus.ACTIVE);
        }

        productionLineRepository.saveAllAndFlush(productionLines);
    }

    @Transactional
    public void decreaseWeeklyMaintenanceCost() {
        List<ProductionLine> productionLines = productionLineRepository.findProductionLinesByStatusEquals(Enums.ProductionLineStatus.ACTIVE);
        for (ProductionLine productionLine : productionLines) {
            Team team = productionLine.getTeam();
            ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.get(productionLine.getProductionLineTemplateId());
            team.setCredit(team.getCredit() - template.getWeeklyMaintenanceCost());
        }

        productionLineRepository.saveAllAndFlush(productionLines);
    }
}
