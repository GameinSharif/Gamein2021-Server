package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class WeekSupplyManager
{
    private final WeekSupplyService weekSupplyService;

    public void updateWeekSupplyPrices(int week)
    {
        Supplier[] suppliers = ReadJsonFilesManager.Suppliers;
        if (week > 5)
        {
            try
            {
                for (Supplier supplier : suppliers)
                {
                    for (Integer productId : supplier.getMaterials())
                    {
                        WeekSupplyDto weekSupplyDtoLastWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), productId, week - 1);
                        WeekSupplyDto weekSupplyDtoTwoWeeksAgo = weekSupplyService.findSpecificWeekSupply(supplier.getId(), productId, week - 2);
                        WeekSupplyDto weekSupplyDtoCurrentWeek = weekSupplyService.findSpecificWeekSupply(supplier.getId(), productId, week);

                        Float lastWeekPrice = weekSupplyDtoLastWeek.getPrice();
                        Integer lastWeekSales = weekSupplyDtoLastWeek.getSales();
                        Float coefficient = weekSupplyDtoCurrentWeek.getCoefficient();
                        Integer twoWeeksAgoSale = weekSupplyDtoTwoWeeksAgo.getSales();

                        Float newPrice = weekSupplyService.weeklyPriceFormula(productId, lastWeekPrice, lastWeekSales, twoWeeksAgoSale, coefficient);
                        weekSupplyDtoCurrentWeek.setPrice(newPrice);
                        System.out.println(newPrice);

                        weekSupplyService.saveOrUpdate(weekSupplyDtoCurrentWeek);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
