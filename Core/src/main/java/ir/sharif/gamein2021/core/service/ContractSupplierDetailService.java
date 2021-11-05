package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.ContractSupplierDetailRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDetailDto;
import ir.sharif.gamein2021.core.domain.entity.ContractSupplierDetail;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractSupplierDetailService extends AbstractCrudService<ContractSupplierDetailDto, ContractSupplierDetail, Integer>
{
    private final ContractSupplierDetailRepository contractSupplierDetailRepository;
    private final ModelMapper modelMapper;

    public ContractSupplierDetailService(ContractSupplierDetailRepository contractSupplierDetailRepository, ModelMapper modelMapper)
    {
        this.contractSupplierDetailRepository = contractSupplierDetailRepository;
        this.modelMapper = modelMapper;
        setRepository(contractSupplierDetailRepository);
    }

    @Transactional
    public ContractSupplierDetailDto update(ContractSupplierDetailDto contractSupplierDetailDto){
        return saveOrUpdate(contractSupplierDetailDto);
    }

    @Transactional(readOnly = true)
    public List<ContractSupplierDetailDto> findByDate(LocalDate date){
        List<ContractSupplierDetail> contractSupplierDetails = contractSupplierDetailRepository.findAllByContractDate(date);
        return contractSupplierDetails.stream().map(e -> modelMapper.map(e, ContractSupplierDetailDto.class))
                .collect(Collectors.toList());
    }

}
