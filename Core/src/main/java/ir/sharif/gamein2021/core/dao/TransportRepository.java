package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface TransportRepository extends JpaRepository<Transport, Integer> {
    List<Transport> findAllByTransportState(Enums.TransportState transportState);
    List<Transport> findAllByStart_date(LocalDate start_date);
    List<Transport> findAllByEnd_dateAndTransportState(LocalDate end_date, Enums.TransportState state);
}
