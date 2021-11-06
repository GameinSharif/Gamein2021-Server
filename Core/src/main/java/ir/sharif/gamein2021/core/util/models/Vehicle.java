package ir.sharif.gamein2021.core.util.models;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private int id;
    private Enums.VehicleType vehicleType;
    private int speed;
    private int capacity;
    private int costPerKilometer;
    private float coefficient;
}
