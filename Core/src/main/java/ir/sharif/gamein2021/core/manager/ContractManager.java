package ir.sharif.gamein2021.core.manager;


import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import ir.sharif.gamein2021.core.domain.entity.WeekDemand;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@AllArgsConstructor
@Component
public class ContractManager
{
    private final ContractSupplierService contractSupplierService;
    private final ContractSupplierDetailService contractSupplierDetailService;
    private final ContractService contractService;
    private final ContractDetailService contractDetailService;
    private final WeekSupplyService weekSupplyService;
    private final WeekDemandService weekDemandService;
    private final TransportManager transportManager;
    private final GameCalendar gameCalendar;

    public void updateContracts()
    {
        LocalDate today = gameCalendar.getCurrentDate();
        updateTodayContractCosts(today);
        buyFromContractsWithGameinCustomers(today);
    }

    public void updateTodayContractCosts(LocalDate today)
    {
        List<ContractSupplierDto> contractSupplierDtos = contractSupplierService.findTodaysContractSupplier(today);
        for (ContractSupplierDto contractSupplierDto : contractSupplierDtos)
        {
            if (!contractSupplierDto.isTerminated())
            {
                try {
                    WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(), contractSupplierDto.getMaterialId(), GameConstants.getWeakNumber());
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

                            //TODO start transport
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("no week supply");
                }
            }
        }
    }

    public void buyFromContractsWithGameinCustomers(LocalDate today)
    {
        List<WeekDemandDto> weekDemands = weekDemandService.findByWeek(GameConstants.getWeakNumber());
        List<ContractDetailDto> contractDetailDtos = contractDetailService.findByDate(today);

        for (WeekDemandDto weekDemandDto : weekDemands)
        {
            List<ContractDetailDto> thisDemandContractDetails = new ArrayList<>();
            for (ContractDetailDto contractDetailDto : contractDetailDtos)
            {
                ContractDto contractDto = contractService.loadById(contractDetailDto.getContractId());
                if (contractDto.isTerminated())
                {
                    continue;
                }

                if (contractDto.getGameinCustomerId().equals(weekDemandDto.getGameinCustomer().getId())
                && contractDto.getProductId().equals(weekDemandDto.getProductId()))
                {
                    thisDemandContractDetails.add(contractDetailDto);
                }
            }

            FinalizeTheContracts(weekDemandDto, contractDetailDtos);
            contractDetailDtos.removeAll(thisDemandContractDetails);
        }
    }

    private void FinalizeTheContracts(WeekDemandDto weekDemandDto, List<ContractDetailDto> contractDetailDtos)
    {
        for (ContractDetailDto contractDetailDto : contractDetailDtos)
        {
            ContractDto contractDto = contractService.loadById(contractDetailDto.getContractId());

            int boughtAmount = calculateBoughtAmount();

            transportManager.createTransport(
                    Enums.VehicleType.TRUCK,
                    Enums.TransportNodeType.FACTORY,
                    contractDto.getTeamId(),
                    Enums.TransportNodeType.GAMEIN_CUSTOMER,
                    contractDto.getGameinCustomerId(),
                    gameCalendar.getCurrentDate(),
                    true,
                    contractDto.getProductId(),
                    boughtAmount);

            contractDetailDto.setBoughtAmount(boughtAmount);
            contractDetailService.saveOrUpdate(contractDetailDto);
        }
    }

    private int calculateBoughtAmount()
    {
        return 1;
    }
}
