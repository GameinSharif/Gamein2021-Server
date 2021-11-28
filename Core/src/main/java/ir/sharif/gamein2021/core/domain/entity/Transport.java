package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transport implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private Enums.VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private Enums.TransportNodeType sourceType;

    @Column(name = "source_id", nullable = false)
    private Integer sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type", nullable = false)
    private Enums.TransportNodeType destinationType;

    @Column(name = "destination_id", nullable = false)
    private Integer destinationId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "has_insurance", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean hasInsurance;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_state", nullable = false)
    private Enums.TransportState transportState;

    @Column(name = "content_product_id", nullable = false)
    private Integer contentProductId;

    @Column(name = "content_product_amount", nullable = false)
    private Integer contentProductAmount;
}
