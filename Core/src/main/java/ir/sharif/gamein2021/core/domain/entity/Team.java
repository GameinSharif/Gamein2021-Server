package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.*;

import javax.persistence.*;

@Table(name = "Team")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Team implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    @Column(name = "factory_id")
    private Integer factoryId;

    @Enumerated(value = EnumType.STRING)
    private Country country;
    //TODO this field should be initialize
    private float credit;


    @Override
    public Integer getId() {
        return id;
    }
}
