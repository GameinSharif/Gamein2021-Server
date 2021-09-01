package ir.sharif.gamein2021.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;


public class BaseEntity {
    Integer id;

    public BaseEntity() {
    }

    public Integer getId() {
        return id;
    }
}
