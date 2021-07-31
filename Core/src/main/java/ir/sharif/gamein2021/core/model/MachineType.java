package ir.sharif.gamein2021.core.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import ir.sharif.gamein2021.core.util.GameConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@JsonAdapter(MachineType.Serializer.class)
public enum MachineType {

    //Changeable
    ENGINE_MACHINE(0, 500000, 25000, 30, 100, true, new Integer[]{0, 15, 0, 15, 0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    GEARBOX_MACHINE(1, 500000, 17000, 21, 100, true, new Integer[]{0, 15, 0, 0, 0, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    RADIO_MACHINE(2, 500000, 8000, 9, 100, true, new Integer[]{5, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    SUV_WHEEL_MACHINE(3, 500000, 20000, 8, 300, true, new Integer[]{0, 0, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    SEDAN_WHEEL_MACHINE(4, 500000, 17000, 6, 300, true, new Integer[]{0, 0, 20, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    SUV_DASHBOARD_MACHINE(5, 500000, 12000, 16, 100, true, new Integer[]{10, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    SEDAN_DASHBOARD_MACHINE(6, 500000, 12000, 14, 100, true, new Integer[]{10, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    SUSPENSION_MACHINE(7, 500000, 17000, 20, 100, true, new Integer[]{0, 10, 0, 10, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    STEERING_WHEEL_MACHINE(8, 500000, 8000, 9, 100, true, new Integer[]{0, 10, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    BRAKES_MACHINE(9, 500000, 8000, 11, 100, true, new Integer[]{5, 0, 20, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 200000),
    DRIVING_SYSTEM_MACHINE(10, 1200000, 80000, 94, 130, true, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 500000),
    ELECTRICAL_SYSTEM_MACHINE(11, 1200000, 28000, 31, 130, true, new Integer[]{0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 500000),
    SUV_BODY_MACHINE(12, 1200000, 120000, 129, 150, true, new Integer[]{0, 0, 0, 0, 25, 200, 0, 0, 0, 2, 1, 0, 0, 0, 4, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 500000),
    SEDAN_BODY_MACHINE(13, 1200000, 100000, 107, 150, true, new Integer[]{0, 0, 0, 0, 20, 150, 0, 0, 0, 2, 1, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 500000),
    CHASSIS_MACHINE(14, 1200000, 60000, 74, 130, true, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, 500000),
    SUV_CAR_MACHINE(15, 5000000, 600000, 831, 150, true, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0}, 1200000),
    SEDAN_CAR_MACHINE(16, 5000000, 550000, 767, 150, true, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0}, 1200000);

    private final int number;
    private final int buyCost;
    private final int sellCost;
    private final int fixedProductionCost;
    private final int productionCostPerUnit;
    private final int productionRate;
    private final int upgradeCost;
    private final boolean isPrimary;
    private final List<Integer> bom;

    MachineType(int number, int buyCost, int fixedProductionCost, int productionCostPerUnit, int productionRate, boolean isPrimary, Integer[] material, int upgradeCost) {
        this.number = number;
        this.buyCost = buyCost;
        this.fixedProductionCost = fixedProductionCost;
        this.productionCostPerUnit = productionCostPerUnit;
        this.productionRate = productionRate;
        this.isPrimary = isPrimary;
        this.bom = new ArrayList<>();
        Collections.addAll(bom, material);
        this.upgradeCost = upgradeCost;
        this.sellCost = (int) (this.buyCost * GameConstants.machineSellCoeff);
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public int getNumber() {
        return number;
    }

    public int getBuyCost() {
        return buyCost;
    }

    public double getFixedProductionCost() {
        return fixedProductionCost;
    }

    public double getProductionCostPerUnit() {
        return productionCostPerUnit;
    }

    public double getProductionRate() {
        return productionRate;
    }

    public List<Integer> getBom() {
        return this.bom;
    }

    public int getSellCost() {
        return sellCost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    static class Serializer implements JsonSerializer<MachineType>, JsonDeserializer<MachineType> {
        @Override
        public JsonElement serialize(MachineType src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("number", src.getNumber());
            return obj;
        }

        @Override
        public MachineType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            try {
                JsonObject object;
                if (json instanceof JsonObject) {
                    object = (JsonObject) json;
                } else {
                    return ENGINE_MACHINE;
                }
                return getMachineTypeByNumber(object.get("number").getAsInt());
            } catch (JsonParseException e) {
                return ENGINE_MACHINE;
            }
        }

        private MachineType getMachineTypeByNumber(int number) {
            for (MachineType machineType : values())
                if (machineType.getNumber() == number) return machineType;
            return ENGINE_MACHINE;
        }
    }

    private static HashMap<String, Float> getBomFromID(int machineId) {
        return null;
    }
}
