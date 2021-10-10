package ir.sharif.gamein2021.core.Service;

import ir.sharif.gamein2021.core.Service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.dao.AuctionRepository;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Auction;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class AuctionService extends AbstractCrudService<AuctionDto, Auction, Integer> {

    private final AuctionRepository auctionRepository;
    private final TeamService teamService;
    private final ModelMapper modelMapper;


    public AuctionService(AuctionRepository auctionRepository, ModelMapper modelMapper, TeamService teamService) {
        this.auctionRepository = auctionRepository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(auctionRepository);
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @Transactional(readOnly = true)
    public AuctionDto loadById(Integer id) {
        Assert.notNull(id, "The id must not be null!");

        return auctionRepository.findById(id)
                .map(e -> modelMapper.map(e, getDtoClass()))
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Transactional
    public void changeWinnerTeam(AuctionDto auctionDto, TeamDto teamDto, Integer offerPrice) {
        //TODO teamDto already has a company exception
        AuctionDto oldAuctionDto = loadById(auctionDto.getId());
        //check if teamDto exist
        TeamDto oldTeamDto = teamService.loadById(teamDto.getId());
        //TODO price is not higher exception
        oldAuctionDto.setWinnerTeam(oldTeamDto.getId());
        oldAuctionDto.setWinnerPrice(offerPrice);
        oldTeamDto.setFactoryId(oldAuctionDto.getFactoryId());
        teamService.saveOrUpdate(oldTeamDto);
        saveOrUpdate(oldAuctionDto);
    }

}
