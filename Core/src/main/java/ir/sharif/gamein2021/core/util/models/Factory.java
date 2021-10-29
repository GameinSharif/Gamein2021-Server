package ir.sharif.gamein2021.core.util.models;

import ir.sharif.gamein2021.core.exception.FactoryNotFoundException;
import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Factory {
    private static List<Factory> allFactories = new ArrayList<>();
    private int id;
    private String name;
    private Country country;
    private double latitude;
    private double longitude;
    private int rawMaterialCapacity;
    private int secondaryMaterialCapacity;
    private int finalMaterialCapacity;

    public static List<Factory> getAllFactories() {
        return allFactories;
    }

    public static Factory findFactoryById(Integer id){
        for(Factory factory : allFactories){
            if(factory.getId() == id)
                return factory;
        }
        throw new FactoryNotFoundException("Factory with id : " +  id + " does not exist");
    }
}
