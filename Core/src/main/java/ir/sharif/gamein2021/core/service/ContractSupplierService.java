package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ContractSupplierRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDetailDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplier;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}