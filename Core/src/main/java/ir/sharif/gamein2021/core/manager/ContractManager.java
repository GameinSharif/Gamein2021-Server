package ir.sharif.gamein2021.core.manager;


import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDetailDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.domain.entity.WeekSupply;
import ir.sharif.gamein2021.core.service.ContractSupplierDetailService;
import ir.sharif.gamein2021.core.service.ContractSupplierService;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.util.GameConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.time.LocalDate;

@AllArgsConstructor
@Component
public class ContractManager
{
    private final ContractSupplierService contractSupplierService;
    private final ContractSupplierDetailService contractSupplierDetailService;
    private final WeekSupplyService weekSupplyService;
    private final GameCalendar gameCalendar;

    public void updateContracts()
    {
        LocalDate today = gameCalendar.getCurrentDate();
        updateTodayContractCosts(today);
    }

    public void updateTodayContractCosts(LocalDate today)
    {
        List<ContractSupplierDto> contractSupplierDtos = contractSupplierService.findTodaysContractSupplier(today);
        for (ContractSupplierDto contractSupplierDto : contractSupplierDtos)
        {
            if (!contractSupplierDto.isTerminated())
            {
                WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(),
                        contractSupplierDto.getMaterialId(), GameConstants.getWeakNumber());
                Float price = weekSupplyDto.getPrice();
                for (ContractSupplierDetailDto contractSupplierDetailDto : contractSupplierService.getContractSupplierDetailDtos(contractSupplierDto))
                {
                    if (contractSupplierDetailDto.getContractDate().equals(today))
                    {
                        // update price with this week's price
                        contractSupplierDetailDto.setPricePerUnit(price);
                        // Add to Supplier's weekly sale
                        weekSupplyDto.setSales(weekSupplyDto.getSales() + contractSupplierDetailDto.getBoughtAmount());
                        weekSupplyService.saveOrUpdate(weekSupplyDto);
                        contractSupplierDetailService.saveOrUpdate(contractSupplierDetailDto);
                    }
                }
            }
        }
    }
}
