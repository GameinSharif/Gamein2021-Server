package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
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
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Factory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService extends AbstractCrudService<AuctionDto, Auction, Integer>
{
    private final AuctionRepository repository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;

    public static List<Factory> RemainedFactories;

    public AuctionService(AuctionRepository repository, ModelMapper modelMapper, TeamService teamService)
    {
        this.repository = repository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public AuctionDto loadById(Integer id)
    {
        Assert.notNull(id, "The id must not be null!");

        return repository.findById(id)
                .map(e -> modelMapper.map(e, getDtoClass()))
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Override
    public List<AuctionDto> list()
    {
        return super.list();
    }

    public List<AuctionDto> readAllAuctionsWithStatus()
    {
        List<AuctionDto> auctions = list();
        List<AuctionDto> responseAuctions = new ArrayList<>();

        for (AuctionDto auctionDto : auctions)
        {
            responseAuctions.add(AuctionDto.builder()
                    .highestBid(auctionDto.getHighestBid())
                    .factoryId(auctionDto.getFactoryId())
                    .highestBidTeamId(auctionDto.getHighestBidTeamId())
                    .auctionBidStatus(auctionDto.getAuctionBidStatus())
                    .build());
        }
        return  responseAuctions;
    }

    @Transactional
    public AuctionDto changeHighestBid(AuctionDto auctionDto, TeamDto teamDto)
    {
        teamDto = teamService.loadById(teamDto.getId());
        auctionDto = loadById(auctionDto.getId());
        int bidPrice = auctionDto.getHighestBid() + GameConstants.Instance.AuctionStepValue;

        if (teamDto.getFactoryId() != null)
        {
            throw new InvalidRequestException("Not allowed to bid for this auction because you already have one factory");
        }
        if (auctionDto.getHighestBid() >= bidPrice)
        {
            throw new InvalidOfferForAuction("" + bidPrice + " is not enough!");
        }
        if (ReadJsonFilesManager.Factories[auctionDto.getFactoryId()].getCountry() == teamDto.getCountry())
        {
            TeamDto oldTeamDto = teamService.loadById(teamDto.getId());
            auctionDto.setHighestBidTeamId(oldTeamDto.getId());
            auctionDto.setHighestBid(bidPrice);
            auctionDto.setBidsCount(auctionDto.getBidsCount() + 1);
            return saveOrUpdate(auctionDto);
        }
        else
        {
            throw new InvalidCountryException("This factory is not in your country");
        }
    }

    @Transactional
    public AuctionDto bidForFirstTimeForThisFactory(TeamDto teamDto, Integer factoryId)
    {
        AuctionDto auctionDto = new AuctionDto();

        if (teamDto.getFactoryId() != null)
        {
            throw new InvalidRequestException("Not allowed to bid for this auction because you already have one factory");
        }
        if (ReadJsonFilesManager.Factories[factoryId].getCountry() == teamDto.getCountry())
        {
            RemainedFactories.removeIf(f -> f.getId() == factoryId);

            auctionDto.setFactoryId(factoryId);
            auctionDto.setHighestBidTeamId(teamDto.getId());
            auctionDto.setHighestBid(GameConstants.Instance.AuctionStartValue);
            auctionDto.setBidsCount(1);
            auctionDto.setAuctionBidStatus(Enums.AuctionBidStatus.Active);
            return saveOrUpdate(auctionDto);
        }
        else
        {
            throw new InvalidCountryException("This factory is not in your country");
        }
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getActiveAuctions()
    {
        return repository.findAllByAuctionBidStatus(Enums.AuctionBidStatus.Active)
                .stream().map(e -> modelMapper.map(e, AuctionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuctionDto findAuctionByFactory(Integer factoryId)
    {
        Assert.notNull(factoryId, "The factoryId must not be null!");
        Auction auction = repository.findByFactoryId(factoryId);
        if (auction == null)
        {
            throw new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + " by factoryId: " + factoryId + " ");
        }
        if (auction.getAuctionBidStatus() == Enums.AuctionBidStatus.Over)
        {
            throw new InvalidRequestException("Auction for this factory is over.");
        }
        else
        {
            return modelMapper.map(auction, AuctionDto.class);
        }
    }

    @Transactional(readOnly = true)
    public void checkAuctionByTeam(Integer teamId)
    {
        Assert.notNull(teamId, "The teamId must not be null!");
        Auction auction = repository.findAuctionByHighestBidTeamId(teamId);
        if (auction != null)
        {
            throw new InvalidRequestException("Not allowed to bid for this auction because you already have one highest bid");
        }
    }

    @Transactional
    public void assignRemainedFactoriesRandomly()
    {
        for (Enums.Country country : Enums.Country.values())
        {
            List<Factory> thisCountryFactories = new ArrayList<>();
            for (Factory factory : RemainedFactories)
            {
                if (factory.getCountry() == country)
                {
                    thisCountryFactories.add(factory);
                }
            }

            List<TeamDto> emptyTeams = teamService.findAllEmptyTeamWithCountry(country);
            Collections.shuffle(emptyTeams);

            for (int i = 0; i < emptyTeams.size(); i++)
            {
                TeamDto emptyTeam = emptyTeams.get(i);
                emptyTeam.setFactoryId(thisCountryFactories.get(i).getId());
                //TODO decrease bidPrice from team's money
                teamService.saveOrUpdate(emptyTeam);
            }
        }
    }

    @Transactional
    public void completeActiveAuctions()
    {
        List<AuctionDto> auctions = getActiveAuctions();
        auctions.forEach(auction ->
        {
            TeamDto winnerTeam = teamService.loadById(auction.getHighestBidTeamId());
            //TODO decrease bidPrice from team's money
            winnerTeam.setFactoryId(auction.getFactoryId());
            teamService.saveOrUpdate(winnerTeam);

            auction.setAuctionBidStatus(Enums.AuctionBidStatus.Over);
            saveOrUpdate(auction);
        });
    }
}