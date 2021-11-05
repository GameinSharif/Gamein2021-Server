package ir.sharif.gamein2021.core.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sharif.gamein2021.core.exception.FactoryNotFoundException;
import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.util.models.Factory;
import ir.sharif.gamein2021.core.util.models.Product;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReadJsonFilesManager
{
    public static Product[] Products;
    public static Factory[] Factories;

    public static void ReadJsonFiles()
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();

            File productsJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Products.json");
            Products = objectMapper.readValue(productsJsonFile, Product[].class);

            File factoriesJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Factories.json");
            Factories = objectMapper.readValue(factoriesJsonFile, Factory[].class);
            AuctionService.RemainedFactories = new ArrayList<>(Arrays.asList(Arrays.copyOf(Factories, Factories.length)));
        }
        catch (IOException ignored)
        {
        }
    }

    public static Product findProductById(Integer id) {
        for (Product product : Products) {
            if (product.getId() == id)
                return product;
        }
        throw new ProductNotFoundException("Product with id: " + id + " does not exist");
    }



    public static Product[] getAllProducts() {
        return Products;
    }

    public static Factory[] getAllFactories() {
        return Factories;
    }


    public static Factory findFactoryById(Integer id){
        for(Factory factory : Factories){
            if(factory.getId() == id)
                return factory;
        }
        throw new FactoryNotFoundException("Factory with id : " +  id + " does not exist");
    }
}