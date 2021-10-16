package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.AuctionRepository;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Auction;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InvalidCountryException;
import ir.sharif.gamein2021.core.exception.InvalidOfferForAuction;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.util.Enums;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
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
            throw new InvalidRequestException("Not allowed to bid for this auction because you already have one factory");
        if(auctionDto.getHighestBidTeam().equals(teamDto.getId()))
            throw new InvalidRequestException("You are not allowed to bid for this auction because you are number of for this auction");
        if (auctionDto.getHighestBid() >= offerPrice)
            throw new InvalidOfferForAuction("" + offerPrice + " is not enough!");
        if (ReadJsonFilesManager.Factories[auctionDto.getFactoryId()].getCountry() == teamDto.getCountry()) {
            TeamDto oldTeamDto = teamService.loadById(teamDto.getId());
            auctionDto.setHighestBidTeam(oldTeamDto.getId());
            auctionDto.setHighestBid(offerPrice);
            auctionDto.setBidsCount(auctionDto.getBidsCount() + 1);
            saveOrUpdate(auctionDto);
        } else {
            throw new InvalidCountryException("This factory is not in your country");
        }
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getAllAuctionsWithTeam() {
        return repository.findAllByAuctionBidStatus(Enums.AuctionBidStatus.Active)
                .stream().map(e -> modelMapper.map(e, AuctionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuctionDto findAuctionByFactory(Integer id) {
        Assert.notNull(id, "The id must not be null!");
        Auction auction = repository.findByFactoryId(id);
        if (auction == null || auction.getAuctionBidStatus() == Enums.AuctionBidStatus.Over)
            throw new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " ");
        else
            return modelMapper.map(auction, AuctionDto.class);
    }
}
