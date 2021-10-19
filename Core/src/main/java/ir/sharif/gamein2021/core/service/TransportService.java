package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.TransportRepository;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.entity.Provider;
import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import ir.sharif.gamein2021.core.util.Enums;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransportService extends AbstractCrudService<TransportDto, Transport, Integer> {
    private final TransportRepository transportRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransportService(TransportRepository transportRepository, ModelMapper modelMapper) {
        this.transportRepository = transportRepository;
        this.modelMapper = modelMapper;
        setRepository(transportRepository);
    }

    public TransportDto save(TransportDto transportDto) {
        // TODO : Exception
        return saveOrUpdate(transportDto);
    }

    public ArrayList<TransportDto> getPendingTransports() {
        List<Transport> transports = transportRepository.findAllByTransportState(Enums.TransportState.PENDING);
        return mapEntityList(transports);
    }

    public ArrayList<TransportDto> getStartingTransports(LocalDate today) {
        List<Transport> transports = transportRepository.findAllByStart_date(today);
        return mapEntityList(transports);
    }

    public ArrayList<TransportDto> getEndingTransports(LocalDate today) {
        List<Transport> transports = transportRepository.findAllByEnd_date(today);
        return mapEntityList(transports);
    }

    private ArrayList<TransportDto> mapEntityList(List<Transport> transports) {
        ArrayList<TransportDto> transportsDtos = new ArrayList<>();
        for (Transport transport : transports) {
            transportsDtos.add(modelMapper.map(transport, TransportDto.class));
        }
        return transportsDtos;
    }
}
