package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.ContractDetailRepository;
import ir.sharif.gamein2021.core.domain.dto.ContractDetailDto;
import ir.sharif.gamein2021.core.domain.entity.ContractDetail;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ContractDetailService extends AbstractCrudService<ContractDetailDto, ContractDetail, Integer>
{
    private final ContractDetailRepository contractDetailRepository;
    private final ModelMapper modelMapper;

    public ContractDetailService(ContractDetailRepository contractDetailRepository, ModelMapper modelMapper)
    {
        this.contractDetailRepository = contractDetailRepository;
        this.modelMapper = modelMapper;
    }
}
