package ir.sharif.gamein2021.core.domain.model;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Factory {
    private static List<Factory> factories = new ArrayList<>();
    private Integer id;
    private Integer x;
    private Integer y;
    private Country country;
    //TODO ect ....

    public static Factory findFactoryById(Integer id) {
        for (Factory factory : factories) {
            if (factory.getId() == id)
                return factory;
        }
        return null;
    }
}
