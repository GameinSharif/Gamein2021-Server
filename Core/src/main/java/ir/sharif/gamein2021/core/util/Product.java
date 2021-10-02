package ir.sharif.gamein2021.core.util;

import lombok.*;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    private int id;
    private int categoryId;
    private int productionLineId;
    private String name;
    private ArrayList<ProductIngredient> ingredientsPerUnit;
}