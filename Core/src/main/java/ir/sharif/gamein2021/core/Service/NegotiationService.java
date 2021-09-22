package ir.sharif.gamein2021.core.Service;


import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.NegotiationRepository;
import ir.sharif.gamein2021.core.dao.UserRepository;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Negotiation;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.domain.entity.User;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.OfferNotFoundException;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.hibernate.type.TrueFalseType;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


public class NegotiationService extends AbstractCrudService<NegotiationDto, Negotiation, Integer>{
    private final NegotiationRepository negotiationRepository;
    private final ModelMapper modelMapper;

    public NegotiationService(NegotiationRepository negotiationRepository, ModelMapper modelMapper){
        this.negotiationRepository = negotiationRepository;
        this.modelMapper = modelMapper;
        setRepository(negotiationRepository);
    }

    @Transactional(readOnly = true)
    public Negotiation findById(Integer id) {
        return getRepository().findById(id).orElseThrow(EntityNotFoundException::new);
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
