package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;

@Table(name = "Team")
@Entity
public class Team
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    public Team()
    {
    }

    public Team(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsername(String name)
    {
        this.username = name;
    }

    @Override
    public String toString()
    {
        return "TeamEntity{" +
                "name='" + username + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
