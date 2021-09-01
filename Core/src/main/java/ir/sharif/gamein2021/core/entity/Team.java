package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;

@Table(name = "Team")
@Entity
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    private String teamName;

    public Team()
    {
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Integer getId()
    {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
