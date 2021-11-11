package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ContractRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.entity.Contract;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService extends AbstractCrudService<ContractDto, Contract, Integer>
{
    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;

    public ContractService(ContractRepository contractRepository, ModelMapper modelMapper)
    {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
        setRepository(contractRepository);
    }

    @Override
    public ContractDto loadById(Integer id)
    {
        return super.loadById(id);
    }

    @Transactional(readOnly = true)
    public List<ContractDto> findByTeam(Team team)
    {
        List<Contract> contracts = contractRepository.findContractsByTeam(team);
        return contracts.stream()
                .map(e -> modelMapper.map(e, ContractDto.class))
                .collect(Collectors.toList());
    }
}
