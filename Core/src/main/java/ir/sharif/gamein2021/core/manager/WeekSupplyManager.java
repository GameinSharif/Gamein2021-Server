package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@AllArgsConstructor
@Component
public class WeekSupplyManager
{
    private final WeekSupplyService weekSupplyService;

    public void updateWeekSupplyPrices(int week){
        Supplier[] suppliers = ReadJsonFilesManager.Suppliers;
        if(week > 5){
            for(Supplier supplier: suppliers) {
                for (Integer material : supplier.getMaterials()) {
                    WeekSupplyDto weekSupplyDtoLastWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week - 1);
                    WeekSupplyDto weekSupplyDtoLastLastWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week - 2);
                    WeekSupplyDto weekSupplyDtoFirstWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, 1);
                    WeekSupplyDto weekSupplyDtoCurrentWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week);
                    Float firstPrice = weekSupplyDtoFirstWeek.getPrice();
                    Float lastWeekPrice = weekSupplyDtoLastWeek.getPrice();
                    Float lastlastWeekPrice = weekSupplyDtoLastLastWeek.getPrice();
                    Float newPrice = (float)(firstPrice* (1 + ((lastWeekPrice/lastlastWeekPrice + GameConstants.Instance.ConstantWeekSupplyPrice) +
                            (lastlastWeekPrice/lastWeekPrice + GameConstants.Instance.ConstantWeekSupplyPrice)*0.05)) * 1); //TODO coefficient
                    weekSupplyDtoCurrentWeek.setPrice(newPrice);
                    weekSupplyService.saveOrUpdate(weekSupplyDtoCurrentWeek);
                }
            }
        }


    }

}
