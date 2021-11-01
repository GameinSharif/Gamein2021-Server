package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.TransportRepository;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportService extends AbstractCrudService<TransportDto, Transport, Integer> {

    private final TransportRepository transportRepository;
    private final ModelMapper modelMapper;
    private final TeamService teamService;
    private final DcService dcService;

    @Autowired
    public TransportService(TransportRepository transportRepository, ModelMapper modelMapper, TeamService teamService, DcService dcService) {
        this.transportRepository = transportRepository;
        this.modelMapper = modelMapper;
        this.teamService = teamService;
        this.dcService = dcService;
        setRepository(transportRepository);
    }

    @Transactional(readOnly = true)
    public ArrayList<TransportDto> getTransportsByState(Enums.TransportState state) {
        List<Transport> transports = transportRepository.findAllByTransportState(state);
        return mapEntityListToDto(transports);
    }

    @Transactional(readOnly = true)
    public ArrayList<TransportDto> getStartingTransports(LocalDate today) {
        List<Transport> transports = transportRepository.findAllByStartDate(today);
        return mapEntityListToDto(transports);
    }

    @Transactional(readOnly = true)
    public ArrayList<TransportDto> getEndingTransports(LocalDate today) {
        List<Transport> transports = transportRepository.findAllByEndDateAndTransportState(today, Enums.TransportState.IN_WAY);
        return mapEntityListToDto(transports);
    }

    private ArrayList<TransportDto> mapEntityListToDto(List<Transport> transports) {
        ArrayList<TransportDto> transportsDtos = new ArrayList<>();
        for (Transport transport : transports) {
            transportsDtos.add(modelMapper.map(transport, TransportDto.class));
        }
        return transportsDtos;
    }

    @Transactional
    public TransportDto changeTransportState(TransportDto transportDto, Enums.TransportState newState)
    {
        transportDto.setTransportState(newState);
        return saveOrUpdate(transportDto);
    }

    public ArrayList<TransportDto> getTransportsByTeam(Integer teamId) {
        Integer factoryId = teamService.findTeamById(teamId).getFactoryId();
        List<Integer> teamDcIds = dcService.getAllDcForTeam(TeamDto.builder().id(teamId).build()).stream().map(DcDto::getId).collect(Collectors.toList());
        List<Transport> teamTransports = transportRepository.findAllByDestinationTypeAndDestinationId(Enums.TransportNodeType.FACTORY, factoryId);
        teamTransports.addAll(transportRepository.findAllBySourceTypeAndSourceId(Enums.TransportNodeType.FACTORY, factoryId));
        teamTransports.addAll(transportRepository.findAllByDestinationTypeAndDestinationIdIn(Enums.TransportNodeType.DC, teamDcIds));
        teamTransports.addAll(transportRepository.findAllBySourceTypeAndSourceIdIn(Enums.TransportNodeType.DC, teamDcIds));
        // TODO : separate by source or destination?
        return mapEntityListToDto(teamTransports);
    }
}
