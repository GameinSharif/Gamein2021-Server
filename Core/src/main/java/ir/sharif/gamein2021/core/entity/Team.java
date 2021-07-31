package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;

@Table(name = "Team")
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String email;

    public Team() {
    }

    public Team(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
