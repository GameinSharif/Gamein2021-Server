package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.OfferRepository;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.entity.Offer;
import ir.sharif.gamein2021.core.exception.OfferNotFoundException;
import ir.sharif.gamein2021.core.util.AssertionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService extends AbstractCrudService<OfferDto , Offer , Integer> {

    private final OfferRepository offerRepository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, TeamService teamService, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(offerRepository);
    }

    @Transactional
    public OfferDto save(OfferDto offerDto) {
        AssertionUtil.assertDtoNotNull(offerDto, Offer.class.getSimpleName());
        var offer = toOffer(offerDto);
        Offer result = getRepository().save(offer);
        return toOfferDto(result);
    }

    @Transactional
    public OfferDto update(Integer offerId, OfferDto newOffer) {
        AssertionUtil.assertIdNotNull(offerId, OfferDto.class.getSimpleName());
        AssertionUtil.assertDtoNotNull(newOffer, Offer.class.getSimpleName());
        var updatedOfferDto = createUpdateOfferDto(offerId, newOffer);
        var updatedOffer = toOffer(updatedOfferDto);
        Offer result = getRepository().save(updatedOffer);
        return toOfferDto(result);
    }

    @Transactional(readOnly = true)
    public Offer findById(Integer id) {
        return getRepository().findById(id).orElseThrow(OfferNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OfferDto> getAllOffers(){
        List<Offer> offers = offerRepository.findAll();
        List<OfferDto> offerDtos = new ArrayList<>();
        for (Offer offer : offers) {
            offerDtos.add(toOfferDto(offer));
        }
        return offerDtos;
    }

    @Transactional(readOnly = true)
    public List<OfferDto> getTeamOffers(Integer teamId) {
        List<Offer> offers = offerRepository.findAll();
        List<OfferDto> offerDtos = new ArrayList<>();
        for (Offer offer : offers) {
            if (offer.getTeam().getId().equals(teamId)) {
                offerDtos.add(toOfferDto(offer));
            }
        }
        return offerDtos;
    }

    private OfferDto createUpdateOfferDto(Integer offerId, OfferDto newOffer) {
        OfferDto oldOffer = loadById(offerId);

        if(newOffer.getOfferDeadline() != null){
            oldOffer.setOfferDeadline(newOffer.getOfferDeadline());
        }
        if(newOffer.getCostPerUnit() != null){
            oldOffer.setCostPerUnit(newOffer.getCostPerUnit());
        }
        if(newOffer.getEarliestExpectedArrival() != null){
            oldOffer.setEarliestExpectedArrival(newOffer.getEarliestExpectedArrival());
        }
        if(newOffer.getTeamId() != null){
            oldOffer.setTeamId(newOffer.getTeamId());
        }
        if(newOffer.getType() != null){
            oldOffer.setType(newOffer.getType());
        }
        if(newOffer.getVolume() != null){
            oldOffer.setVolume(newOffer.getVolume());
        }
        if(newOffer.getLatestExpectedArrival() != null){
            oldOffer.setLatestExpectedArrival(newOffer.getEarliestExpectedArrival());
        }
        return oldOffer;
    }

    public Offer toOffer(OfferDto offerDto) {
        return Offer.builder()
                .id(offerDto.getId())
                .team(teamService.findById(offerDto.getTeamId()))
                .type(offerDto.getType())
                .volume(offerDto.getVolume())
                .earliestExpectedArrival(offerDto.getEarliestExpectedArrival())
                .latestExpectedArrival(offerDto.getLatestExpectedArrival())
                .costPerUnit(offerDto.getCostPerUnit())
                .offerDeadline(offerDto.getOfferDeadline()).build();
    }

    public OfferDto toOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .teamName(offer.getTeam().getTeamName())
                .type(offer.getType())
                .volume(offer.getVolume())
                .earliestExpectedArrival(offer.getEarliestExpectedArrival())
                .latestExpectedArrival(offer.getLatestExpectedArrival())
                .costPerUnit(offer.getCostPerUnit())
                .offerDeadline(offer.getOfferDeadline()).build();
    }

}
