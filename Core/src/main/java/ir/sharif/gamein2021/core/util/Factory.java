package ir.sharif.gamein2021.core.util;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Factory
{
    private int id;
    private String name;
    private Country country;
    private double latitude;
    private double longitude;
}
