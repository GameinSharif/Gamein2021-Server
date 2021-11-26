package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class WeekSupplyManager
{
    private final WeekSupplyService weekSupplyService;

    public void updateWeekSupplyPrices(int week){
        Supplier[] suppliers = ReadJsonFilesManager.Suppliers;
        if(week > 5){
            try {
                for(Supplier supplier: suppliers) {
                    for (Integer material : supplier.getMaterials()) {
                        WeekSupplyDto weekSupplyDtoLastWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week - 1);
                        WeekSupplyDto weekSupplyDtoLastLastWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week - 2);
                        WeekSupplyDto weekSupplyDtoCurrentWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), material, week);
                        Float lastWeekPrice = weekSupplyDtoLastWeek.getPrice();
                        Integer lastWeekSales = weekSupplyDtoLastWeek.getSales();
                        Integer lastLastWeekSales = weekSupplyDtoLastLastWeek.getSales();
                        Float newPrice = weekSupplyService.weeklyPriceFormula(lastWeekPrice, lastWeekSales, lastLastWeekSales);
                        weekSupplyDtoCurrentWeek.setPrice(newPrice);
                        weekSupplyService.saveOrUpdate(weekSupplyDtoCurrentWeek);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

}
