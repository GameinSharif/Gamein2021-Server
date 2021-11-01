package ir.sharif.gamein2021.core.util.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private int id;
    private String name;
    private int speed;
    private int capacity;
    private int costPerKilometer;
    private int coefficient;
}
