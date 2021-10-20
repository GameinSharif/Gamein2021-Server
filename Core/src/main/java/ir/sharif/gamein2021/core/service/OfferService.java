package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.entity.Contract;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.OfferRepository;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.exception.OfferNotFoundException;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService extends AbstractCrudService<OfferDto, Offer, Integer>
{

    private final OfferRepository offerRepository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, TeamService teamService, ModelMapper modelMapper)
    {
        this.offerRepository = offerRepository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(offerRepository);
    }

    @Transactional(readOnly = true)
    public Offer findById(Integer id)
    {
        return getRepository().findById(id).orElseThrow(OfferNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OfferDto> findByTeam(Team team)
    {
        List<Offer> offers = offerRepository.findOffersByTeam(team);
        return offers.stream()
                .map(e -> modelMapper.map(e, OfferDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OfferDto> findOffersExceptTeam(Team team)
    {
        List<Offer> offers = offerRepository.findAllByTeamIsNot(team);
        return offers.stream()
                .map(e -> modelMapper.map(e, OfferDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public OfferDto addOffer(OfferDto offerDto)
    {
        AssertionUtil.assertDtoNotNull(offerDto, Offer.class.getSimpleName());
        return saveOrUpdate(offerDto);
    }

    @Override
    public void delete(Integer id)
    {
        super.delete(id);
    }
}
