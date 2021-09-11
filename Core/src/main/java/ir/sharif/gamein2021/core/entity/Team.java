package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;
import java.util.List;

@Table(name = "Team")
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String teamName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private List<User> users;

    public Team()
    {
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
