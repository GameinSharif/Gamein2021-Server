package ir.sharif.gamein2021.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BaseEntity {
    @Id
    Integer id;

    public BaseEntity() {
    }

    public Integer getId() {
        return id;
    }
}
