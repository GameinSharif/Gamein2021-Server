package ir.sharif.gamein2021.core.service;


import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.NegotiationRepository;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.domain.entity.Negotiation;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NegotiationService extends AbstractCrudService<NegotiationDto, Negotiation, Integer>{
    private final NegotiationRepository negotiationRepository;
    private final ModelMapper modelMapper;

    public NegotiationService(NegotiationRepository negotiationRepository, ModelMapper modelMapper){
        this.negotiationRepository = negotiationRepository;
        this.modelMapper = modelMapper;
        setRepository(negotiationRepository);
    }

    @Transactional(readOnly = true)
    public NegotiationDto findById(Integer id) {
        return modelMapper.map(getRepository().findById(id).orElseThrow(EntityNotFoundException::new), NegotiationDto.class);
    }

    @Transactional(readOnly = true)
    public ArrayList<NegotiationDto> findByTeam(Team team) {
        ArrayList<NegotiationDto> negotiationDtos = new ArrayList<>();
        List<Negotiation> negotiations = negotiationRepository.findAllByDemanderOrSupplier(team , team);
        for(Negotiation element: negotiations){
            negotiationDtos.add(modelMapper.map(element,NegotiationDto.class));
        }
        return negotiationDtos;

        //return negotiationRepository.findNegotiationByDemanderOrSupplier(team);
    }

    @Transactional
    public NegotiationDto save(NegotiationDto negotiationDto){
        //TODO Exceptions
        return saveOrUpdate(negotiationDto);
    }

}
