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
    private int id;
    private String name;
    private Country country;
    private double latitude;
    private double longitude;
}
