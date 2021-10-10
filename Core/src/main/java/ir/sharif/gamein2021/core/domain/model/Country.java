package ir.sharif.gamein2021.core.domain.model;

public enum Country {
    //This is just an example
    Iran(0);

    private int value;

    Country(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public static Country getCountryById(int id){
        for(Country country : Country.values()) {
            if (country.getValue() == id) {
                return country;
            }
        }
        return null;
    }
}
