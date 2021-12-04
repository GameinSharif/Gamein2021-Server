package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
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
    private final TeamManager teamManager;

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
            if (contractSupplierDto.getIsTerminated() || contractSupplierDto.getTransportationCost() != null)
            {
                continue;
            }
            try
            {
                TeamDto team = teamService.loadById(contractSupplierDto.getTeamId());
                WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(contractSupplierDto.getSupplierId(), contractSupplierDto.getMaterialId(), gameCalendar.getCurrentWeek());

                finalizeTheContractWithSupplier(contractSupplierDto, team, weekSupplyDto, false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean finalizeTheContractWithSupplier(ContractSupplierDto contractSupplierDto, TeamDto team, WeekSupplyDto weekSupplyDto, boolean isCreating)
    {
        float price = weekSupplyDto.getPrice();
        contractSupplierDto.setPricePerUnit(price);

        Enums.VehicleType vehicleType = contractSupplierDto.getTransportType();
        Integer supplierId = contractSupplierDto.getSupplierId();
        Boolean hasInsurance = contractSupplierDto.getHasInsurance();
        Integer materialId = contractSupplierDto.getMaterialId();
        Integer amount = contractSupplierDto.getBoughtAmount();

        float transportCost = transportManager.calculateTransportCost(
                vehicleType,
                transportManager.getTransportDistance(
                        Enums.TransportNodeType.SUPPLIER,
                        supplierId,
                        Enums.TransportNodeType.FACTORY,
                        team.getFactoryId(),
                        vehicleType),
                materialId,
                amount,
                hasInsurance);

        float totalPrice = price * contractSupplierDto.getBoughtAmount() + transportCost;

        float teamCredit = team.getCredit();
        if (totalPrice > teamCredit)
        {
            if (!isCreating)
            {
                team.setCredit(teamCredit - contractSupplierDto.getNoMoneyPenalty());
                team.setWealth(team.getWealth() - contractSupplierDto.getNoMoneyPenalty());
                teamService.saveOrUpdate(team);
            }
            return false;
        }

        transportManager.createTransport(
                vehicleType,
                Enums.TransportNodeType.SUPPLIER,
                supplierId,
                Enums.TransportNodeType.FACTORY,
                team.getFactoryId(),
                gameCalendar.getCurrentDate(),
                hasInsurance,
                materialId,
                amount);

        team.setCredit(teamCredit - totalPrice);
        team.setWealth(team.getWealth() - totalPrice);
        team.setOutFlow(team.getOutFlow() + totalPrice);
        team.setTransportationCost(team.getTransportationCost() + transportCost);
        teamService.saveOrUpdate(team);

        weekSupplyDto.setSales(weekSupplyDto.getSales() + contractSupplierDto.getBoughtAmount());
        weekSupplyService.saveOrUpdate(weekSupplyDto);

        contractSupplierDto.setTransportationCost(transportCost);
        contractSupplierService.saveOrUpdate(contractSupplierDto);

        return true;
    }

    public void buyFromContractsWithGameinCustomers(LocalDate today)
    {
        List<WeekDemandDto> weekDemands = weekDemandService.findByWeek(gameCalendar.getCurrentWeek());

        for (WeekDemandDto weekDemandDto : weekDemands)
        {
            GameinCustomerDto gameinCustomerDto = gameinCustomerService.loadById(weekDemandDto.getGameinCustomerId());
            Product product = ReadJsonFilesManager.findProductById(weekDemandDto.getProductId());
            List<ContractDto> contractDtos = contractService.findValidContracts(today, gameinCustomerDto, product);
            if (contractDtos == null || contractDtos.size() == 0)
            {
                weekDemandDto.setRemainedAmount(weekDemandDto.getAmount());
                weekDemandService.saveOrUpdate(weekDemandDto);
                continue;
            }
            FinalizeTheContracts(weekDemandDto, contractDtos);
        }
    }

    private void FinalizeTheContracts(WeekDemandDto weekDemandDto, List<ContractDto> contractDtos)
    {
        if (contractDtos == null || contractDtos.size() == 0)
        {
            return;
        }

        TreeMap<Float, ContractDto> treeMap = new TreeMap<>(Collections.reverseOrder());
        float totalShares = 0f;

        float maxPrice = contractDtos.get(0).getPricePerUnit();
        float minPrice = contractDtos.get(0).getPricePerUnit();

        for (ContractDto contractDto : contractDtos)
        {
            StorageDto storageDto = storageService.loadById(contractDto.getStorageId());

            float B = contractDto.getCurrentBrand();
            float P = contractDto.getPricePerUnit();
            int d = transportManager.getTransportDistance(
                    storageDto.getDc() ? Enums.TransportNodeType.DC : Enums.TransportNodeType.FACTORY,
                    storageDto.getBuildingId(),
                    Enums.TransportNodeType.GAMEIN_CUSTOMER,
                    weekDemandDto.getGameinCustomerId(),
                    Enums.VehicleType.TRUCK);

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

        float equalShareAmount = 1f / contractDtos.size();
        float totalIncome = 0f;
        int remainedDemand = weekDemandDto.getAmount();
        for (Map.Entry<Float, ContractDto> entry : treeMap.entrySet())
        {
            float sharePercent = entry.getKey() / totalShares;
            ContractDto contractDto = entry.getValue();
            StorageDto storageDto = storageService.loadById(contractDto.getStorageId());

            int sale = (int) Math.floor(Math.min(contractDto.getSupplyAmount(), weekDemandDto.getAmount() * sharePercent));

            TeamDto teamDto = teamService.loadById(contractDto.getTeamId());

            StorageProductDto storageProductDto = storageService.getStorageProductWithBuildingId(
                    storageDto.getBuildingId(),
                    storageDto.getDc(),
                    contractDto.getProductId());

            if (storageProductDto != null && storageProductDto.getAmount() >= sale)
            {
                remainedDemand -= sale;
                contractDto.setBoughtAmount(sale);
                float income = sale * contractDto.getPricePerUnit();
                totalIncome += income;
                teamDto.setCredit(teamDto.getCredit() + income);
                teamDto.setWealth(teamDto.getWealth() + income);
                teamDto.setInFlow(teamDto.getInFlow() + income);

                if (sharePercent >= equalShareAmount)
                {
                    teamManager.updateTeamBrand(teamDto, sharePercent - equalShareAmount + GameConstants.brandIncreaseAfterFinalizeContractWithCustomer);
                }
            }
            else
            {
                contractDto.setBoughtAmount(0);
                teamDto.setCredit(teamDto.getCredit() - contractDto.getLostSalePenalty());
                teamDto.setWealth(teamDto.getWealth() - contractDto.getLostSalePenalty());
            }
            teamService.saveOrUpdate(teamDto);

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
            if (weekDemandDto.getAmount() == remainedDemand || totalIncome == 0)
            {
                contractDto.setDemandShare(0f);
                contractDto.setValueShare(0f);
            }
            else
            {
                contractDto.setDemandShare(100f * contractDto.getBoughtAmount() / (weekDemandDto.getAmount() - remainedDemand));
                contractDto.setValueShare(100f * income / totalIncome);
            }
            contractService.saveOrUpdate(contractDto);

            if (contractDto.getBoughtAmount() == 0)
            {
                continue;
            }

            StorageDto storageDto = storageService.loadById(contractDto.getStorageId());

            transportManager.createTransport(
                    Enums.VehicleType.TRUCK,
                    storageDto.getDc() ? Enums.TransportNodeType.DC : Enums.TransportNodeType.FACTORY,
                    storageDto.getBuildingId(),
                    Enums.TransportNodeType.GAMEIN_CUSTOMER,
                    contractDto.getGameinCustomerId(),
                    gameCalendar.getCurrentDate(),
                    true,
                    contractDto.getProductId(),
                    contractDto.getBoughtAmount());
        }
    }
}
