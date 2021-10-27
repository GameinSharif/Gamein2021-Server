package ir.sharif.gamein2021.core.util.models;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier
{
    private int id;
    private Country country;
    private ArrayList<Integer> materials;
    private ArrayList<Integer> pricePerUnit;
    private double latitude;
    private double longitude;
}
