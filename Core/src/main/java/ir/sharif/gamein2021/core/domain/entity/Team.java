package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "Team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Team implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;




    @Override
    public Integer getId() {
        return id;
    }
}
