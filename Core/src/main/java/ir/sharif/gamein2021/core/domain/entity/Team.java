package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.domain.model.Country;
import lombok.*;

import javax.persistence.*;

@Table(name = "Team")
@Data
@NoArgsConstructor
@Builder
@Entity
public class Team implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    private Integer randomCountryIndex;

    private int shopXCoordinate , shopYCoordinate = 0;
    @Enumerated(value = EnumType.ORDINAL)
    private Country country;

    @Override
    public Integer getId() {
        return id;
    }
}
