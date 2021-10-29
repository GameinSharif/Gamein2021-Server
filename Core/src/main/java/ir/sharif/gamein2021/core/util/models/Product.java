package ir.sharif.gamein2021.core.util.models;

import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    private static List<Product> allProducts = new ArrayList<>();
    private int id;
    private String categoryIds; //for SemiFinishedProducts only
    private int productionLineId; //for SemiFinishedProducts & Finished only
    private Enums.ProductType productType;
    private String name;
    private int volumetricUnit;
    private ArrayList<ProductIngredient> ingredientsPerUnit; //for SemiFinishedProducts & Finished only except CarbonDioxide

    public static Product findProductById(Integer id){
        for(Product product : allProducts){
            if(product.getId() == id)
                return product;
        }
        throw new ProductNotFoundException("Product with id: " +  id + " does not exist");
    }
}