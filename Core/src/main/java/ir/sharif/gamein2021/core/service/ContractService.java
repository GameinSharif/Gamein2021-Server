package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.entity.GameinCustomer;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ContractRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.entity.Contract;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService extends AbstractCrudService<ContractDto, Contract, Integer>
{
    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final TransportManager transportManager;

    @Autowired
    public ContractService(ContractRepository contractRepository, ModelMapper modelMapper, TransportManager transportManager)
    {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
        this.transportManager = transportManager;
        setRepository(contractRepository);
    }

    @Override
    public ContractDto loadById(Integer id)
    {
        return super.loadById(id);
    }

    @Transactional(readOnly = true)
    public List<ContractDto> findByTeamAndTerminatedIsFalse(Team team)
    {
        List<Contract> contracts = contractRepository.findContractsByTeamAndIsTerminatedIsFalse(team);
        return contracts.stream()
                .map(e -> modelMapper.map(e, ContractDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContractDto> findByDate(LocalDate date)
    {
        List<Contract> contracts = contractRepository.findContractsByContractDate(date);
        return contracts.stream()
                .map(e -> modelMapper.map(e, ContractDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContractDto> findValidContracts(LocalDate date, GameinCustomerDto gameinCustomerDto, Product product)
    {
        GameinCustomer gameinCustomer = modelMapper.map(gameinCustomerDto, GameinCustomer.class);
        List<Contract> contracts = contractRepository.findContractsByGameinCustomerAndProductIdAndContractDateAndIsTerminatedIsFalse(gameinCustomer, product.getId(), date);

        for(int i = contracts.size() - 1; i >= 0; i--)
        {
            int transportDurationDays = transportManager.calculateTransportDuration(
                    Enums.TransportNodeType.FACTORY,
                    contracts.get(i).getTeam().getId(), //TODO use storage
                    Enums.TransportNodeType.GAMEIN_CUSTOMER,
                    gameinCustomer.getId(),
                    Enums.VehicleType.TRUCK
            );

            if (transportDurationDays > 7)
            {
                contracts.remove(i);
            }
        }

        return contracts.stream()
                .map(e -> modelMapper.map(e, ContractDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContractDto findByTeamAndDateAndGameinCustomerAndProduct(Team team, LocalDate date, GameinCustomerDto gameinCustomerDto, Product product)
    {
        GameinCustomer gameinCustomer = modelMapper.map(gameinCustomerDto, GameinCustomer.class);
        Contract contract = contractRepository.findContractByTeamAndGameinCustomerAndProductIdAndContractDate(team, gameinCustomer, product.getId(), date);
        if (contract == null)
        {
            return null;
        }
        return modelMapper.map(contract, ContractDto.class);
    }
}
