package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.DcRepository;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.entity.Dc;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.exception.DcHasOwnerException;
import ir.sharif.gamein2021.core.exception.EntityNotFoundException;
import ir.sharif.gamein2021.core.exception.InactiveDcException;
import ir.sharif.gamein2021.core.exception.InvalidRequestException;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.GameConstants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DcService extends AbstractCrudService<DcDto, Dc, Integer> {
    private final DcRepository repository;
    private final ModelMapper modelMapper;
    private final TeamService teamService;

    public DcService(DcRepository repository, ModelMapper modelMapper, TeamService teamService) {
        this.repository = repository;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        setRepository(repository);
    }

    @Transactional(readOnly = true)
    public DcDto loadById(Integer id) {
        Assert.notNull(id, "The id must not be null!");

        return repository.findById(id)
                .map(e -> {
                    DcDto dcDto = modelMapper.map(e, getDtoClass());
                    if (e.getOwner() != null)
                        dcDto.setOwnerId(e.getOwner().getId());
                    return dcDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("can not find entity " + getEntityClass().getSimpleName() + "by id: " + id + " "));
    }

    @Override
    public List<DcDto> list() {
        return repository.findAll()
                .stream()
                .map(e -> {
                    DcDto dcDto = modelMapper.map(e, getDtoClass());
                    if (e.getOwner() != null)
                        dcDto.setOwnerId(e.getOwner().getId());
                    return dcDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public DcDto buyDc(DcDto dc, TeamDto teamDto) {
        teamDto = teamService.loadById(teamDto.getId());
        dc = loadById(dc.getId());
        if (!isActive(dc))
            throw new InactiveDcException("Dc with id " + dc.getId() + " is not active yet");
        //TODO check if dc need to be in the same country
        //TODO check if we need any more additional if
        if (dc.getOwnerId() != null) {
            throw new DcHasOwnerException("Dc with id " + dc.getId() + " has been bought by " + dc.getOwnerId());
        }
        if (teamDto.getCredit() < dc.getBuyingPrice()) {
            throw new InvalidRequestException("Not enough money for buying dc with id " + dc.getId());
        }
        int credit = teamDto.getCredit() - dc.getBuyingPrice();
        teamDto.setCredit(credit);
        dc.setOwnerId(teamDto.getId());
        saveOrUpdate(dc);
        teamService.saveOrUpdate(teamDto);
        return loadById(dc.getId());
    }

    @Transactional
    public DcDto sellDc(DcDto dc, TeamDto teamDto) {
        teamDto = teamService.loadById(teamDto.getId());
        dc = loadById(dc.getId());
        if (!dc.getOwnerId().equals(teamDto.getId())) {
            throw new DcHasOwnerException("Dc with id " + dc.getId() + " is not yours to sell ");
        }
        if (!isActive(dc))
            throw new InactiveDcException("Dc with id " + dc.getId() + " is not active yet");
        int credit = teamDto.getCredit() + dc.getSellingPrice();
        teamDto.setCredit(credit);
        dc.setOwnerId(null);
        saveOrUpdate(dc);
        teamService.saveOrUpdate(teamDto);
        return loadById(dc.getId());
    }

    @Override
    public DcDto saveOrUpdate(DcDto dcDto) {
        Assert.notNull(dcDto, "The dc must not be null!");
        if (!isActive(dcDto))
            throw new InactiveDcException("Dc with id " + dcDto.getId() + " is not active yet");
        Dc dc = modelMapper.map(dcDto, getEntityClass());
        if (dcDto.getOwnerId() != null) {
            dc.setOwner(teamService.findTeamById(dcDto.getOwnerId()));
        }
        else
            dc.setOwner(null);
        Dc result = repository.save(dc);
        log.debug("save/update entity {}", result);
        DcDto resultDto = modelMapper.map(result, getDtoClass());
        resultDto.setOwnerId(dcDto.getOwnerId());
        return resultDto;
    }

    @Transactional(readOnly = true)
    public boolean isActive(DcDto dc) {
        Assert.notNull(dc, "This dc must not be null!");
        dc = loadById(dc.getId());
        if (dc.getStartingWeak() <= GameConstants.getWeakNumber())
            return true;
        return false;
    }

    @Transactional(readOnly = true)
    public List<DcDto> getAllActiveDc() {
        return repository.findAllByStartingWeakIsLessThanEqual(GameConstants.getWeakNumber())
                .stream().map(e -> {
                    DcDto dcDto = modelMapper.map(e, DcDto.class);
                    if (e.getOwner() != null)
                        dcDto.setOwnerId(e.getOwner().getId());
                    return dcDto;
                }).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<DcDto> getAllDcForTeam(TeamDto teamDto) {
        Team team = teamService.findTeamById(teamDto.getId());
        return repository.findAllByOwner(team).stream().map(e -> {
            DcDto dcDto = modelMapper.map(e, DcDto.class);
            if (e.getOwner() != null)
                dcDto.setOwnerId(e.getOwner().getId());
            return dcDto;
        }).collect(Collectors.toList());
    }
}
