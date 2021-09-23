package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
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

@Service
public class OfferService extends AbstractCrudService<OfferDto , Offer , Integer> {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        setRepository(offerRepository);
    }

    @Transactional
    public OfferDto save(OfferDto offerDto) {
        AssertionUtil.assertDtoNotNull(offerDto, Offer.class.getSimpleName());
        var offer = modelMapper.map(offerDto, Offer.class);
        Offer result = getRepository().save(offer);
        return modelMapper.map(result, OfferDto.class);
    }

    @Transactional
    public OfferDto update(Integer offerId, OfferDto newOffer) {
        AssertionUtil.assertIdNotNull(offerId, OfferDto.class.getSimpleName());
        AssertionUtil.assertDtoNotNull(newOffer, Offer.class.getSimpleName());
        var updatedUserDto = createUpdateOfferDto(offerId, newOffer);
        var updatedUser = modelMapper.map(updatedUserDto, Offer.class);
        Offer result = getRepository().save(updatedUser);
        return modelMapper.map(result, OfferDto.class);
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
            offerDtos.add(modelMapper.map(offer, OfferDto.class));
        }
        return offerDtos;
    }

    @Transactional(readOnly = true)
    public List<OfferDto> getTeamOffers(Integer teamId) {
        List<Offer> offers = offerRepository.findAll();
        List<OfferDto> offerDtos = new ArrayList<>();
        for (Offer offer : offers) {
            if (offer.getTeam().getId().equals(teamId)) {
                offerDtos.add(modelMapper.map(offer, OfferDto.class));
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
        if(newOffer.getTeam() != null){
            oldOffer.setTeam(newOffer.getTeam());
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

    public OfferDto requestToDto(NewOfferRequest newOfferRequest) {
        OfferDto offerDto = new OfferDto();
        offerDto.setTeam(offerRepository.findById(newOfferRequest.getTeamId()).isPresent().get();
        offerDto.setType(newOfferRequest.getType());
        offerDto.setVolume(newOfferRequest.getVolume());
        offerDto.setCostPerUnit(newOfferRequest.getCostPerUnit());
        offerDto.setEarliestExpectedArrival(newOfferRequest.getEarliestExpectedArrival());
        offerDto.setLatestExpectedArrival(newOfferRequest.getLatestExpectedArrival());
        offerDto.setOfferDeadline(newOfferRequest.getOfferDeadline());
        return offerDto;
    }

}
