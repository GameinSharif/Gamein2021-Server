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
    @Column(nullable = false)
    private boolean isFirstTime = true;
    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;
    @Column(name = "factory_id")
    private Integer factoryId;
    @Enumerated(value = EnumType.ORDINAL) //TODO String
    private Country country;

    @Override
    public Integer getId() {
        return id;
    }
}
