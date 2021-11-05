package ir.sharif.gamein2021.core.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.util.models.Vehicle;
import ir.sharif.gamein2021.core.util.models.Factory;
import ir.sharif.gamein2021.core.util.models.Product;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class ReadJsonFilesManager
{
    public static Product[] Products;
    public static Vehicle[] Vehicles;
    public static Factory[] Factories;
    public static Supplier[] Suppliers;
    public static ProductionLineTemplate[] ProductionLineTemplates;

    public static void ReadJsonFiles()
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();

            File productsJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Products.json");
            Products = objectMapper.readValue(productsJsonFile, Product[].class);

            File vehiclesJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Vehicles.json");
            Vehicles = objectMapper.readValue(vehiclesJsonFile, Vehicle[].class);

            File factoriesJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Factories.json");
            Factories = objectMapper.readValue(factoriesJsonFile, Factory[].class);
            AuctionService.RemainedFactories = new ArrayList<>(Arrays.asList(Arrays.copyOf(Factories, Factories.length)));

            File productionLineTemplateJsonFile = ResourceUtils.getFile("classpath:JsonFiles/ProductionLineTemplates.json");
            ProductionLineTemplates = objectMapper.readValue(productionLineTemplateJsonFile, ProductionLineTemplate[].class);

            File suppliersJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Suppliers.json");
            Suppliers = objectMapper.readValue(suppliersJsonFile, Supplier[].class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}