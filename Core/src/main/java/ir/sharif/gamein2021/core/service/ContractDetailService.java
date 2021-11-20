package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ContractDetailRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractDetailDto;
import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractDetailService extends AbstractCrudService<ContractDetailDto, ContractDetail, Integer>
{
    private final ContractDetailRepository contractDetailRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ContractDetailService(ContractDetailRepository contractDetailRepository, ModelMapper modelMapper)
    {
        this.contractDetailRepository = contractDetailRepository;
        this.modelMapper = modelMapper;
        setRepository(contractDetailRepository);
    }

    @Transactional(readOnly = true)
    public List<ContractDetailDto> findByDate(LocalDate date)
    {
        List<ContractDetail> contractDetails = contractDetailRepository.findAllByContractDate(date);
        return contractDetails.stream().map(e -> modelMapper.map(e, ContractDetailDto.class))
                .collect(Collectors.toList());
    }
}
