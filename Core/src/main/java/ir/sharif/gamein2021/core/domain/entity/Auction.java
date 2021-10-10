package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //This field is for factories and because factories are not entity this should be an id
    //Also this field should be unique and not null
    @Column(nullable = false, unique = true, name = "factory_id")
    private Integer factoryId;
    @Column(name = "winner-price")
    private int winnerPrice;
    @OneToOne
    @Column(unique = true, name = "winner_team", nullable = true)
    private Team winnerTeam;
    //TODO if auction should have maximum or minimum
}
