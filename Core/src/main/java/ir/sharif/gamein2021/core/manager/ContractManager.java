package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.time.LocalDate;

@AllArgsConstructor
@Component
public class ContractManager
{
    private final ContractSupplierService contractSupplierService;
    private final ContractService contractService;
    private final TeamService teamService;
    private final WeekSupplyService weekSupplyService;
    private final WeekDemandService weekDemandService;
    private final TransportManager transportManager;
    private final GameCalendar gameCalendar;

    public void updateContracts()
    {
        LocalDate today = gameCalendar.getCurrentDate();
        updateTodayContractCosts(today);
    }

    public void updateGameinCustomerContracts()
    {
        LocalDate today = gameCalendar.getCurrentDate();
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
                    WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(), contractSupplierDto.getMaterialId(), gameCalendar.getCurrentWeek());
                    Float price = weekSupplyDto.getPrice();
                    contractSupplierDto.setPricePerUnit(price);
                    Enums.VehicleType vehicleType = contractSupplierDto.getTransportType();
                    Integer supplierId = contractSupplierDto.getSupplierId();
                    boolean hasInsurance = contractSupplierDto.isHasInsurance();
                    Integer materialId = contractSupplierDto.getMaterialId();
                    Integer amount = contractSupplierDto.getBoughtAmount();
                    TransportDto transportDto = transportManager.createTransport(
                            vehicleType,
                            Enums.TransportNodeType.SUPPLIER,
                            supplierId,
                            Enums.TransportNodeType.FACTORY,
                            teamService.loadById(contractSupplierDto.getTeamId()).getFactoryId(),
                            gameCalendar.getCurrentDate(),
                            hasInsurance,
                            materialId,
                            amount);
                    Integer distance = transportManager.getTransportDistance(transportDto);
                    contractSupplierDto.setTransportationCost(transportManager.calculateTransportCost(transportDto.getVehicleType(),
                            distance, materialId, amount, hasInsurance));
                    weekSupplyDto.setSales(weekSupplyDto.getSales() + contractSupplierDto.getBoughtAmount());
                    weekSupplyService.saveOrUpdate(weekSupplyDto);
                    contractSupplierService.saveOrUpdate(contractSupplierDto);

                    /*for (ContractSupplierDetailDto contractSupplierDetailDto : contractSupplierService.getContractSupplierDetailDtos(contractSupplierDto))
                    {
                        if (contractSupplierDetailDto.getContractDate().equals(today))
                        {
                            // update price with this week's price
                            //contractSupplierDetailDto.setPricePerUnit(price);
                            // Add to Supplier's weekly sale
                            weekSupplyDto.setSales(weekSupplyDto.getSales() + contractSupplierDetailDto.getBoughtAmount());
                            weekSupplyService.saveOrUpdate(weekSupplyDto);
                            contractSupplierDetailService.saveOrUpdate(contractSupplierDetailDto);

                            //TODO start transport
                        }
                    }*/
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
        List<WeekDemandDto> weekDemands = weekDemandService.findByWeek(gameCalendar.getCurrentWeek());
        List<ContractDto> contractDtos = contractService.findByDate(today);

        for (WeekDemandDto weekDemandDto : weekDemands)
        {
            List<ContractDto> thisDemandContractDtos = contractService.findByDate(today);
            for (ContractDto contractDto : contractDtos)
            {
                if (contractDto.getIsTerminated())
                {
                    continue;
                }

                if (contractDto.getGameinCustomerId().equals(weekDemandDto.getGameinCustomerId())
                    && contractDto.getProductId().equals(weekDemandDto.getProductId()))
                {
                    thisDemandContractDtos.add(contractDto);
                }
            }

            FinalizeTheContracts(weekDemandDto, thisDemandContractDtos);
            contractDtos.removeAll(thisDemandContractDtos);
        }
    }

    private void FinalizeTheContracts(WeekDemandDto weekDemandDto, List<ContractDto> contractDtos)
    {
        for (ContractDto contractDto : contractDtos)
        {
            int boughtAmount = calculateBoughtAmount();

            //TODO check player has those amount of product
            //TODO if not calculate lost sale penalty and decrease from player money

            contractDto.setBoughtAmount(boughtAmount);
            //TODO set share, ...

            contractService.saveOrUpdate(contractDto);

            transportManager.createTransport(
                    Enums.VehicleType.TRUCK,
                    Enums.TransportNodeType.FACTORY,
                    teamService.findTeamById(contractDto.getTeamId()).getFactoryId(),
                    Enums.TransportNodeType.GAMEIN_CUSTOMER,
                    contractDto.getGameinCustomerId(),
                    gameCalendar.getCurrentDate(),
                    true,
                    contractDto.getProductId(),
                    boughtAmount);
        }
    }

    private int calculateBoughtAmount()
    {
        return 100;
    }
}
