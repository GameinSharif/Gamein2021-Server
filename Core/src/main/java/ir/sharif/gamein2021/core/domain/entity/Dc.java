package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Team owner;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "buying_price", nullable = false)
    private int buyingPrice;

    @Column(name = "selling_price", nullable = false)
    private int sellingPrice;

    @Column(name = "starting_week", nullable = false)
    private int startingWeek;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Enums.DCType type;
}
