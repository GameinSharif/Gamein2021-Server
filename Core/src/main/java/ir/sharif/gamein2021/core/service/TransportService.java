package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.dao.TransportRepository;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.service.core.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
