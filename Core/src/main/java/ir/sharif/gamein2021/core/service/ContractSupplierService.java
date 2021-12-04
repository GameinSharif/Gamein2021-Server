package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ContractSupplierRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractSupplierService extends AbstractCrudService<ContractSupplierDto, ContractSupplier, Integer>
{
    private final ContractSupplierRepository contractSupplierRepository;
    private final ModelMapper modelMapper;

    public ContractSupplierService(ContractSupplierRepository contractSupplierRepository, ModelMapper modelMapper)
    {
        this.contractSupplierRepository = contractSupplierRepository;
        this.modelMapper = modelMapper;
        setRepository(contractSupplierRepository);
    }

    public Supplier SupplierIdValidation(Integer supplierId)
    {
        for(Supplier s : ReadJsonFilesManager.Suppliers)
        {
            if (s.getId() == supplierId)
            {
                return s;
            }
        }
        return null;
    }

    @Transactional
    public ContractSupplierDto save(ContractSupplierDto contractSupplierDto)
    {
        return saveOrUpdate(contractSupplierDto);
    }

    @Transactional
    public ContractSupplierDto update(ContractSupplierDto contractSupplierDto)
    {
        return saveOrUpdate(contractSupplierDto);
    }

    @Transactional(readOnly = true)
    public ContractSupplierDto findById(Integer id)
    {
        return modelMapper.map(getRepository().findById(id).orElseThrow(EntityNotFoundException::new), ContractSupplierDto.class);
    }

    @Transactional(readOnly = true)
    public List<ContractSupplierDto> findByTeamAndNotTerminated(Team team)
    {
        List<ContractSupplier> contractSuppliers = contractSupplierRepository.findByTeamAndIsTerminatedFalse(team);
        return contractSuppliers.stream().map(e -> modelMapper.map(e, ContractSupplierDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContractSupplierDto> findTodaysContractSupplier(LocalDate today)
    {
        List<ContractSupplier> contractSuppliers = contractSupplierRepository.findAllByContractDate(today);
        return contractSuppliers.stream().map(e -> modelMapper.map(e, ContractSupplierDto.class))
                .collect(Collectors.toList());
    }

}
