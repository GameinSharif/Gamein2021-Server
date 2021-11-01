package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Integer> {
    List<Transport> findAllByTransportState(Enums.TransportState transportState);
    List<Transport> findAllByStartDate(LocalDate start_date);
    List<Transport> findAllByEndDateAndTransportState(LocalDate end_date, Enums.TransportState state);
    List<Transport> findAllBySourceTypeAndSourceId(Enums.TransportNodeType sourceType, Integer sourceId);
    List<Transport> findAllByDestinationTypeAndDestinationId(Enums.TransportNodeType destinationType, Integer destinationId);
    List<Transport> findAllBySourceTypeAndSourceIdIn(Enums.TransportNodeType sourceType, List<Integer> sourceId);
    List<Transport> findAllByDestinationTypeAndDestinationIdIn(Enums.TransportNodeType destinationType, List<Integer> destinationId);
}
