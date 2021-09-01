package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;

@Entity
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Integer getId(){
        return id;
    }
}
