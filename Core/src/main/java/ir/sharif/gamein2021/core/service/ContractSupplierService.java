package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ContractSupplierDetailRepository;
import ir.sharif.gamein2021.core.dao.ContractSupplierRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDetailDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractSupplierService extends AbstractCrudService<ContractSupplierDto, ContractSupplier, Integer>
{
    private final ContractSupplierRepository contractSupplierRepository;
    private final ContractSupplierDetailRepository contractSupplierDetailRepository;
    private final ModelMapper modelMapper;

    public ContractSupplierService(ContractSupplierRepository contractSupplierRepository, ContractSupplierDetailRepository contractSupplierDetailRepository,
                                   ModelMapper modelMapper)
    {
        this.contractSupplierRepository = contractSupplierRepository;
        this.contractSupplierDetailRepository = contractSupplierDetailRepository;
        this.modelMapper = modelMapper;
        setRepository(contractSupplierRepository);
    }

    public Supplier SupplierIdValidation(Integer supplierId)
    {
        return ReadJsonFilesManager.Suppliers[supplierId];
    }

    @Transactional
    public ContractSupplierDto save(ContractSupplierDto contractSupplierDto, List<ContractSupplierDetailDto> contractSupplierDetailDtos) {
        for(ContractSupplierDetailDto contractDetailDto: contractSupplierDetailDtos){
            ContractSupplierDetail contractSupplierDetail = modelMapper.map(contractDetailDto, ContractSupplierDetail.class);
            contractSupplierDto.getContractSupplierDetails().add(contractSupplierDetail);
        }
        return saveOrUpdate(contractSupplierDto);
    }

    @Transactional
    public ContractSupplierDto update(ContractSupplierDto contractSupplierDto){
        return saveOrUpdate(contractSupplierDto);
    }

    @Transactional(readOnly = true)
    public ContractSupplierDto findById(Integer id) {
        return modelMapper.map(getRepository().findById(id).orElseThrow(EntityNotFoundException::new), ContractSupplierDto.class);
    }

    @Transactional(readOnly = true)
    public List<ContractSupplierDto> findByTeamId(Integer teamId){
        List<ContractSupplier> contractSuppliers = contractSupplierRepository.findByTeamId(teamId);
        return contractSuppliers.stream().map(e -> modelMapper.map(e, ContractSupplierDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContractSupplierDto> findTodaysContractSupplier(LocalDate today){
        List<ContractSupplierDto> contractSupplierDtos = new ArrayList<>();
        List<ContractSupplierDetail> contractSupplierDetails = contractSupplierDetailRepository.findAllByContractDate(today);
        for(ContractSupplierDetail contractDetail: contractSupplierDetails){
            contractSupplierDtos.add(modelMapper.map(contractSupplierRepository.findByContractSupplierDetail(contractDetail), ContractSupplierDto.class));
        }
        return contractSupplierDtos;
    }

    public List<ContractSupplierDetailDto> getContractSupplierDetailDtos(ContractSupplierDto contractSupplierDto){
        return contractSupplierDto.getContractSupplierDetails().stream().map(e -> modelMapper.map(e, ContractSupplierDetailDto.class))
                .collect(Collectors.toList());
    }

}
