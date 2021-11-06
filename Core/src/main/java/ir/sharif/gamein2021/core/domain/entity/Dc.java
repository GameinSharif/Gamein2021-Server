package ir.sharif.gamein2021.core.domain.entity;

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
    private int buyingPrice;
    private int sellingPrice;
    @Column(name = "starting_weak")
    private int startingWeak;
    private double latitude;
    private double longitude;
    private int capacity;
    @OneToOne
    private Storage storage;
    private boolean isRawMaterial;
}
