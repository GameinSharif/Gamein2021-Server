package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineProductRepository;
import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.dao.TeamRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.dto.StorageProductDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineIdException;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineTemplateIdException;
import ir.sharif.gamein2021.core.exception.ProductionLineMaximumEfficiencyLevelReachedException;
import ir.sharif.gamein2021.core.exception.ProductionLineMaximumQualityLevelReachedException;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ProductionLinesConstructionCompletedRequest;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Product;
import ir.sharif.gamein2021.core.util.models.ProductIngredient;
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
    private final StorageService storageService;
    private final ClientHandlerRequestSenderInterface clientHandlerRequestSender;

    public ProductionLineService(ProductionLineRepository productionLineRepository,
                                 ProductionLineProductRepository productRepository,
                                 ModelMapper modelMapper,
                                 GameCalendar gameCalendar,
                                 TeamRepository teamRepository,
                                 StorageService storageService,
                                 ClientHandlerRequestSenderInterface clientHandlerRequestSender) {
        setRepository(productionLineRepository);
        this.clientHandlerRequestSender = clientHandlerRequestSender;
        this.storageService = storageService;
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

        productionLine.setActivationDate(gameCalendar.getCurrentDate().plusDays(template.getConstructRequiredDays()));
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
        teamObject.setWealth(teamObject.getWealth() - template.getConstructionCost() + scrapPrice);

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

        Product productTemplate = ReadJsonFilesManager.ProductHashMap.getOrDefault(productId, null);
        if (productTemplate == null) {
            throw new InvalidProductionLineIdException("Invalid operation. No Product exists with specified product id.");
        }
        if (productTemplate.getProductionLineTemplateId() != productionLine.getProductionLineTemplateId()) {
            throw new InvalidProductionLineIdException("Selected productLine is not able to create selected product");
        }

        amount = amount * productLineTemplate.getBatchSize();

        float productionCost = productLineTemplate.getSetupCost() + amount * productLineTemplate.getProductionCostPerOneProduct();

        float newCredit = team.getCredit() - productionCost;
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to produce new production.");
        }

        if (productTemplate.getId() != 27) //CarbonDioxide
        {
            for (ProductIngredient productIngredient : productTemplate.getIngredientsPerUnit()) {
                if (isWater(productIngredient.getProductId())) {
                    continue;
                }

                StorageProductDto storageDto = storageService.findProductStorageByIdNull(storageService.findStorageWithBuildingIdAndDc(team.getFactoryId(), false), productIngredient.getProductId());
                if (storageDto == null || storageDto.getAmount() < amount * productIngredient.getAmount()) {
                    throw new InvalidProductionLineIdException("Production requirements are not available in storage.");
                }
            }

            for (ProductIngredient productIngredient : productTemplate.getIngredientsPerUnit()) {
                if (isWater(productIngredient.getProductId())) {
                    newCredit -= getWaterCost(amount);
                    team.setUsedWater(team.getUsedWater() + (long) amount * productIngredient.getAmount());
                    continue;
                }

                storageService.deleteProducts(team.getFactoryId(), false, productIngredient.getProductId(), amount * productIngredient.getAmount());
            }
        }

        team.setCredit(newCredit);
        team.setWealth(team.getWealth() - productionCost);
        team.setProductionCost(team.getProductionCost() + productionCost);

        teamRepository.saveAndFlush(team);

        int productionDuration = (int) Math.ceil(1f * amount / productLineTemplate.getDailyProductionRate());

        ProductionLineProduct newProduct = new ProductionLineProduct();
        newProduct.setProductId(productId);
        newProduct.setStartDate(currentDate);
        newProduct.setAmount(amount);
        newProduct.setEndDate(currentDate.plusDays(productionDuration));
        newProduct.setProductionLineId(productionLineId);

        ProductionLineProduct savedProduct = productRepository.saveAndFlush(newProduct);
        productionLine.getProducts().add(savedProduct);
        return modelMapper.map(productionLine, ProductionLineDto.class);
    }

    private float getWaterCost(Integer amount) {
        Product water = ReadJsonFilesManager.ProductHashMap.get(4);
        if (gameCalendar.getCurrentWeek() < 51) {
            return water.getMinPrice() * amount;
        }

        return water.getMaxPrice() * amount;
    }

    private boolean isWater(int productId) {
        return productId == 4;
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

        float upgradeCost = template.getQualityLevels().get(currentQualityLevel + 1).getUpgradeCost();
        float newCredit = team.getCredit() - upgradeCost;
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to upgrade quality level.");
        }

        team.setCredit(newCredit);
        team.setWealth(team.getWealth() - upgradeCost);

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

        float upgradeCost = template.getEfficiencyLevels().get(currentEfficiencyLevel + 1).getUpgradeCost();
        float newCredit = team.getCredit() - upgradeCost;
        if (newCredit < 0) {
            throw new InvalidProductionLineIdException("Invalid Operation. You don't have enough money to upgrade efficiency level.");
        }
        team.setCredit(newCredit);
        team.setWealth(team.getWealth() - upgradeCost);

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

        List<ProductionLineDto> savedProductionLines = saveAll(productionLines);

        clientHandlerRequestSender.send(new ProductionLinesConstructionCompletedRequest(savedProductionLines, "Done"));
    }

    @Transactional
    public void decreaseWeeklyMaintenanceCost() {
        List<ProductionLine> productionLines = productionLineRepository.findProductionLinesByStatusEquals(Enums.ProductionLineStatus.ACTIVE);
        for (ProductionLine productionLine : productionLines) {
            Team team = productionLine.getTeam();
            ProductionLineTemplate template = ReadJsonFilesManager.ProductionLineTemplateHashMap.get(productionLine.getProductionLineTemplateId());
            team.setCredit(team.getCredit() - template.getWeeklyMaintenanceCost());
            team.setWealth(team.getWealth() - template.getWeeklyMaintenanceCost());
        }

        productionLineRepository.saveAllAndFlush(productionLines);
    }
}
