package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.WeekSupplyRepository;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.domain.entity.WeekSupply;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeekSupplyService extends AbstractCrudService<WeekSupplyDto, WeekSupply, Integer>
{
    private final WeekSupplyRepository weekSupplyRepository;
    private final ModelMapper modelMapper;

    public WeekSupplyService(WeekSupplyRepository weekSupplyRepository, ModelMapper modelMapper)
    {
        this.weekSupplyRepository = weekSupplyRepository;
        this.modelMapper = modelMapper;
        setRepository(weekSupplyRepository);
    }

    @Transactional(readOnly = true)
    public List<WeekSupplyDto> findByWeek(Integer week)
    {
        List<WeekSupply> weekSupplies = weekSupplyRepository.findAllByWeek(week);
        return weekSupplies.stream()
                .map(e -> modelMapper.map(e, WeekSupplyDto.class))
                .collect(Collectors.toList());
    }

    public WeekSupplyDto findSpecificWeekSupply(Integer supplierId, Integer materialId, Integer week)
    {
        WeekSupply weekSupply = weekSupplyRepository.findAllBySupplierIdAndProductIdAndWeek(supplierId, materialId, week);
        return modelMapper.map(weekSupply, WeekSupplyDto.class);
    }

    public Float weeklyPriceFormula(int productId, Float lastWeekPrice, Integer lastWeekSale, Integer twoWeeksAgoSale, Float coefficient)
    {
        Product product = ReadJsonFilesManager.findProductById(productId);

        float changeAmount = (lastWeekSale / (twoWeeksAgoSale + GameConstants.ConstantOneWeekSupplyPrice) -
                twoWeeksAgoSale / (lastWeekSale + GameConstants.ConstantOneWeekSupplyPrice)) *
                GameConstants.ConstantTwoWeekSupplyPrice;

        float newPrice = lastWeekPrice * (1 + changeAmount) * coefficient;
        newPrice = Math.min(newPrice, product.getMaxPrice());
        newPrice = Math.max(newPrice, product.getMinPrice());

        return newPrice;
    }
}
