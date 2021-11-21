package ir.sharif.gamein2021.core.manager;


import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import ir.sharif.gamein2021.core.domain.entity.User;
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
    private final TransportService transportService;
    private final ContractSupplierService contractSupplierService;
    private final ContractService contractService;
    private final ContractDetailService contractDetailService;
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
        System.out.println("in update!!");
        List<ContractSupplierDto> contractSupplierDtos = contractSupplierService.findTodaysContractSupplier(today);
        for (ContractSupplierDto contractSupplierDto : contractSupplierDtos)
        {
            System.out.println("in for looooop");
            if (!contractSupplierDto.isTerminated())
            {
                System.out.println("not term");
                try {
                    float teamCredit = teamService.findTeamById(contractSupplierDto.getTeamId()).getCredit();
                    WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(), contractSupplierDto.getMaterialId(), gameCalendar.getWeek());
                    Float price = weekSupplyDto.getPrice();
                    TeamDto team = teamService.loadById(contractSupplierDto.getTeamId());
                    if(price > teamCredit){
                        System.out.println("You don't have enough money! Costs you a penalty..");
                        team.setCredit(teamCredit - contractSupplierDto.getNoMoneyPenalty());
                        teamService.saveOrUpdate(team);
                        continue;
                    }
                    contractSupplierDto.setPricePerUnit(price);
                    teamCredit -= price;
                    team.setCredit(teamCredit);
                    teamService.saveOrUpdate(team);
                    Enums.VehicleType vehicleType = contractSupplierDto.getTransportType();
                    Integer supplierId = contractSupplierDto.getSupplierId();
                    boolean hasInsurance = contractSupplierDto.isHasInsurance();
                    Integer materialId = contractSupplierDto.getMaterialId();
                    Integer amount = contractSupplierDto.getBoughtAmount();

                    weekSupplyDto.setSales(weekSupplyDto.getSales() + contractSupplierDto.getBoughtAmount());
                    weekSupplyService.saveOrUpdate(weekSupplyDto);
                    contractSupplierService.saveOrUpdate(contractSupplierDto);

                    // Creating transport for this contract
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
                    if(contractSupplierDto.getTransportationCost() > teamCredit){
                        System.out.println("You don't have enough money for transport! Costs you a penalty..");
                        transportDto.setTransportState(Enums.TransportState.TERMINATED);
                        team.setCredit(teamCredit - contractSupplierDto.getTerminatePenalty());
                        teamService.saveOrUpdate(team);
                        continue;
                    }
                    teamCredit -= contractSupplierDto.getTerminatePenalty();
                    team.setCredit(teamCredit);
                    teamService.saveOrUpdate(team);
                    System.out.println("transport on its way");
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
        List<WeekDemandDto> weekDemands = weekDemandService.findByWeek(gameCalendar.getWeek());
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

                if (contractDto.getGameinCustomerId().equals(weekDemandDto.getGameinCustomerId())
                && contractDto.getProductId().equals(weekDemandDto.getProductId()))
                {
                    thisDemandContractDetails.add(contractDetailDto);
                }
            }

            FinalizeTheContracts(weekDemandDto, thisDemandContractDetails);
            contractDetailDtos.removeAll(thisDemandContractDetails);
        }
    }

    private void FinalizeTheContracts(WeekDemandDto weekDemandDto, List<ContractDetailDto> contractDetailDtos)
    {
        for (ContractDetailDto contractDetailDto : contractDetailDtos)
        {
            ContractDto contractDto = contractService.loadById(contractDetailDto.getContractId());

            int boughtAmount = calculateBoughtAmount();

            //TODO check player has those amount of product
            //TODO if not calculate lost sale penalty and decrease from player money

            contractDetailDto.setBoughtAmount(boughtAmount);
            contractDetailService.saveOrUpdate(contractDetailDto);

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
