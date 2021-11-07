package ir.sharif.gamein2021.core.util.models;

import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private static final List<Product> allProducts = new ArrayList<>();
    private int id;
    private String categoryIds; //for SemiFinishedProducts only
    private int productionLineTemplateId; //for SemiFinishedProducts & Finished only
    private Enums.ProductType productType;
    private String name;
    private int volumetricUnit;
    private ArrayList<ProductIngredient> ingredientsPerUnit; //for SemiFinishedProducts & Finished only except CarbonDioxide
    private int minPrice;
    private int maxPrice;

}