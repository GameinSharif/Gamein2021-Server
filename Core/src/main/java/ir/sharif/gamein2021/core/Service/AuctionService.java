package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.AuctionRepository;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Auction;
import ir.sharif.gamein2021.core.domain.model.Country;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InvalidCountryException;
import ir.sharif.gamein2021.core.exception.InvalidOfferForAuction;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService extends AbstractCrudService<AuctionDto, Auction, Integer> {

    private final AuctionRepository repository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;


    public AuctionService(AuctionRepository repository, ModelMapper modelMapper, TeamService teamService) {
        this.repository = repository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public AuctionDto loadById(Integer id) {
        Assert.notNull(id, "The id must not be null!");

        return repository.findById(id)
                .map(e -> modelMapper.map(e, getDtoClass()))
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Transactional
    public void changeHigherTeam(AuctionDto auctionDto, TeamDto teamDto, Integer offerPrice) {
        teamDto = teamService.loadById(teamDto.getId());
        auctionDto = loadById(auctionDto.getId());
        if (teamDto.getFactoryId() != null)
            throw new InvalidRequestException("Not allowed to have another factory");
        if (auctionDto.getHigherPrice() >= offerPrice)
            throw new InvalidOfferForAuction("" + offerPrice + " is not enough!");
        if (auctionDto.getCountry() == teamDto.getCountry()) {
            TeamDto oldTeamDto = teamService.loadById(teamDto.getId());
            auctionDto.setHigherTeamId(oldTeamDto.getId());
            auctionDto.setHigherPrice(offerPrice);
            saveOrUpdate(auctionDto);
        } else {
            throw new InvalidCountryException("This factory is not in your country");
        }
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getAllAuctionsWithTeam() {
        return repository.findAllByHigherTeamIdIsNotNull()
                .stream().map(e -> modelMapper.map(e, AuctionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuctionDto findAuctionByFactory(Integer id) {
        Assert.notNull(id, "The id must not be null!");
        Auction auction = repository.findByFactoryId(id);
        if (auction == null)
            throw new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " ");
        else
            return modelMapper.map(auction, AuctionDto.class);
    }


    @Transactional
    public void completeAllAuctions() {
        completeAvailableAuctions();
        for (Country country : Country.getCountries()) {

            List<AuctionDto> allUnFinishedAuctionsByCountry = repository.
                    findAllByHigherTeamIdIsNotNullAndCountry(country).stream().map(e -> modelMapper.map(e, AuctionDto.class))
                    .collect(Collectors.toList());

            List<TeamDto> emptyTeams = teamService.findAllEmptyTeamWithCountry(country);

            Collections.shuffle(allUnFinishedAuctionsByCountry);
            Collections.shuffle(emptyTeams);

            for (int i = 0; i < emptyTeams.size(); i++) {
                TeamDto emptyTeam = emptyTeams.get(i);
                AuctionDto auctionDto = allUnFinishedAuctionsByCountry.get(i);
                emptyTeam.setFactoryId(auctionDto.getFactoryId());
                auctionDto.setHigherTeamId(emptyTeam.getId());
                //TODO setting auction highest price and payments
                teamService.saveOrUpdate(emptyTeam);
                saveOrUpdate(auctionDto);
            }
        }
    }

    @Transactional
    public void completeAvailableAuctions() {
        getAllAuctionsWithTeam().forEach(e -> {
            TeamDto winnerTeam = teamService.loadById(e.getHigherTeamId());
            //TODO Payment
            winnerTeam.setFactoryId(e.getFactoryId());
            teamService.saveOrUpdate(winnerTeam);
        });
    }

}
