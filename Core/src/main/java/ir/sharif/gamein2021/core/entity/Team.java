package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;

@Table(name = "Team")
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamName;
    private String password;

    public Team() {
    }

    public Team(String name, String password) {
        this.teamName = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() { return teamName; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTeamName(String name) { this.teamName = name; }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "name='" + teamName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
