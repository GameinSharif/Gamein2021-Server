package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ProductionLineRepository;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineIdException;
import ir.sharif.gamein2021.core.exception.InvalidProductionLineTemplateIdException;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionLineService extends AbstractCrudService<ProductionLineDto, ProductionLine, Integer> {
    private final ProductionLineRepository productionLineRepository;
    private final ModelMapper modelMapper;

    public ProductionLineService(ProductionLineRepository productionLineRepository, ModelMapper modelMapper) {
        setRepository(productionLineRepository);
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

    @Transactional(readOnly = true)
    public ProductionLineDto CreateProductionLine(ProductionLineDto productionLine) throws InvalidProductionLineTemplateIdException {
        Set<Integer> productLineTemplateIds = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).map(ProductionLineTemplate::getId).collect(Collectors.toSet());

        if (!productLineTemplateIds.contains(productionLine.getProductionLineTemplateId())) {
            throw new InvalidProductionLineTemplateIdException("Invalid productionLineTemplateId used for creating productionLine");
        }

        return saveOrUpdate(productionLine);
    }

    @Transactional(readOnly = true)
    public ProductionLineDto scrapProductionLine(Team team, Integer productionLineId) throws InvalidProductionLineIdException {
        ProductionLine productionLine = productionLineRepository.findById(productionLineId).orElse(null);

        if (productionLine == null) {
            throw new InvalidProductionLineIdException("No production line founded with id = " + productionLineId);
        }

        if (!productionLine.getTeam().getId().equals(team.getId())) {
            throw new InvalidProductionLineIdException("Production line with id = " + productionLineId + " doesn't belong to team with id = " + team.getId());
        }

        if (productionLine.getStatus() != Enums.ProductionLineStatus.ACTIVE) {
            throw new InvalidProductionLineIdException("Invalid Operation. Tried to scrap a production line with not active status");
        }

        // TODO check no product is in production.

        // TODO what to with scarp price. better to use e dictionary for saving productionLineTemplates.
        ProductionLineTemplate template = Arrays.stream(ReadJsonFilesManager.ProductionLineTemplates).filter(x->x.getId() == productionLine.getProductionLineTemplateId()).findFirst().orElse(null);
        int scrapPrice = template.getScrapPrice();

        productionLine.setStatus(Enums.ProductionLineStatus.SCRAPPED);
        ProductionLine savedProductionLine = productionLineRepository.save(productionLine);
        return modelMapper.map(savedProductionLine, ProductionLineDto.class);
    }
}
