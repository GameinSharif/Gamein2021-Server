package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;

import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.time.LocalDate;

@AllArgsConstructor
@Component
public class ContractManager
{
    private final ContractSupplierService contractSupplierService;
    private final ContractService contractService;
    private final TeamService teamService;
    private final StorageService storageService;
    private final GameinCustomerService gameinCustomerService;
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
        System.out.println(contractSupplierDtos.size());
        for (ContractSupplierDto contractSupplierDto : contractSupplierDtos)
        {
            if (!contractSupplierDto.isTerminated())
            {
                try {
                    float teamCredit = teamService.findTeamById(contractSupplierDto.getTeamId()).getCredit();
                    WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(), contractSupplierDto.getMaterialId(), gameCalendar.getWeek());
                    Float price = weekSupplyDto.getPrice();
                    TeamDto team = teamService.loadById(contractSupplierDto.getTeamId());
                    if(price > teamCredit){
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
                    Integer distance = transportManager.getTransportDistance(Enums.TransportNodeType.SUPPLIER, supplierId,
                            Enums.TransportNodeType.FACTORY, teamService.loadById(contractSupplierDto.getTeamId()).getFactoryId(), vehicleType);
                    contractSupplierDto.setTransportationCost(transportManager.calculateTransportCost(vehicleType,
                            distance, materialId, amount, hasInsurance));
                    if(contractSupplierDto.getTransportationCost() > teamCredit){
                        team.setCredit(teamCredit - contractSupplierDto.getTerminatePenalty());
                        teamService.saveOrUpdate(team);
                        continue;
                    }
                    // Creating transport for this contract
                    transportManager.createTransport(
                            vehicleType,
                            Enums.TransportNodeType.SUPPLIER,
                            supplierId,
                            Enums.TransportNodeType.FACTORY,
                            teamService.loadById(contractSupplierDto.getTeamId()).getFactoryId(),
                            gameCalendar.getCurrentDate(),
                            hasInsurance,
                            materialId,
                            amount);
                    teamCredit -= contractSupplierDto.getTransportationCost();
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
        List<WeekDemandDto> weekDemands = weekDemandService.findByWeek(gameCalendar.getCurrentWeek());

        for (WeekDemandDto weekDemandDto : weekDemands)
        {
            GameinCustomerDto gameinCustomerDto =gameinCustomerService.loadById(weekDemandDto.getGameinCustomerId());
            Product product = ReadJsonFilesManager.findProductById(weekDemandDto.getProductId());
            List<ContractDto> contractDtos = contractService.findValidContracts(today, gameinCustomerDto, product);
            FinalizeTheContracts(weekDemandDto, contractDtos);
        }
    }

    private void FinalizeTheContracts(WeekDemandDto weekDemandDto, List<ContractDto> contractDtos)
    {
        TreeMap<Float, ContractDto> treeMap = new TreeMap<>(Collections.reverseOrder());
        float totalShares = 0f;
        float maxPrice = contractDtos.get(0).getMaxPrice();
        float minPrice = contractDtos.get(0).getMinPrice();

        for (ContractDto contractDto : contractDtos)
        {
            TeamDto teamDto = teamService.loadById(contractDto.getTeamId());

            float B = contractDto.getCurrentBrand();
            float P = contractDto.getPricePerUnit();
            int d = transportManager.getTransportDistance(Enums.TransportNodeType.FACTORY, teamDto.getFactoryId(), Enums.TransportNodeType.GAMEIN_CUSTOMER, weekDemandDto.getGameinCustomerId(), Enums.VehicleType.TRUCK);

            float share = B / (GameConstants.ShareAllocationAlpha * P + GameConstants.ShareAllocationBeta * d);
            treeMap.put(share, contractDto);
            totalShares += share;

            if (P > maxPrice)
            {
                maxPrice = P;
            }
            else if (P < minPrice)
            {
                minPrice = P;
            }
        }

        float totalIncome = 0f;
        int remainedDemand = weekDemandDto.getAmount();
        for (Map.Entry<Float, ContractDto> entry : treeMap.entrySet())
        {
            float sharePercent = entry.getKey() / totalShares;
            ContractDto contractDto = entry.getValue();

            int sale = (int) Math.floor(Math.min(contractDto.getSupplyAmount(), weekDemandDto.getAmount() * sharePercent));

            TeamDto teamDto = teamService.loadById(contractDto.getTeamId());
            StorageProductDto storageProductDto = storageService.getStorageProductWithBuildingId(teamDto.getFactoryId(), false, contractDto.getProductId());
            if (storageProductDto.getAmount() >= sale)
            {
                remainedDemand -= sale;
                contractDto.setBoughtAmount(sale);
                float income = sale * contractDto.getPricePerUnit();
                totalIncome += income;
                teamDto.setCredit(teamDto.getCredit() + income);
                teamService.saveOrUpdate(teamDto);
            }
            else
            {
                contractDto.setBoughtAmount(0);
            }

            if (remainedDemand == 0)
            {
                break;
            }
        }
        weekDemandDto.setRemainedAmount(remainedDemand);
        weekDemandService.saveOrUpdate(weekDemandDto);

        for (Map.Entry<Float, ContractDto> entry : treeMap.entrySet())
        {
            ContractDto contractDto = entry.getValue();
            float income = contractDto.getBoughtAmount() * contractDto.getPricePerUnit();

            contractDto.setMaxPrice(maxPrice);
            contractDto.setMinPrice(minPrice);
            contractDto.setValueShare(income / totalIncome);
            contractDto.setDemandShare(1f * contractDto.getBoughtAmount() / (weekDemandDto.getAmount() - remainedDemand));
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
                    contractDto.getBoughtAmount());
        }
    }
}
