package ir.sharif.gamein2021.core.domain.model;

import java.util.ArrayList;

public enum Country {
    //This is just an example
    IRAN,
    GERMANY;

    private static final Country[] countries = {Country.IRAN , Country.GERMANY};

    public static Country[] getCountries() {
        return countries;
    }
}
