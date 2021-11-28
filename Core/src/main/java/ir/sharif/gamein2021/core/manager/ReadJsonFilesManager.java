package ir.sharif.gamein2021.core.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sharif.gamein2021.core.exception.FactoryNotFoundException;
import ir.sharif.gamein2021.core.exception.ProductNotFoundException;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.models.Vehicle;
import ir.sharif.gamein2021.core.util.models.Factory;
import ir.sharif.gamein2021.core.util.models.Product;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class ReadJsonFilesManager {
    public static Product[] Products;
    public static HashMap<Integer, Product> ProductHashMap = new HashMap<>();
    public static Vehicle[] Vehicles;
    public static Factory[] Factories;
    public static Supplier[] Suppliers;
    public static ProductionLineTemplate[] ProductionLineTemplates;
    public static HashMap<Integer, ProductionLineTemplate> ProductionLineTemplateHashMap = new HashMap<>();

    public static void ReadJsonFiles() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Resource productsJsonFile = new ClassPathResource("JsonFiles/Products.json");
            Products = objectMapper.readValue(productsJsonFile.getInputStream(), Product[].class);
            for (Product product: Products){
                ProductHashMap.put(product.getId(), product);
            }

            Resource vehiclesJsonFile = new ClassPathResource("JsonFiles/Vehicles.json");
            Vehicles = objectMapper.readValue(vehiclesJsonFile.getInputStream(), Vehicle[].class);

            Resource factoriesJsonFile = new ClassPathResource("JsonFiles/Factories.json");
            Factories = objectMapper.readValue(factoriesJsonFile.getInputStream(), Factory[].class);

            Resource productionLineTemplateJsonFile = new ClassPathResource("JsonFiles/ProductionLineTemplates.json");
            ProductionLineTemplates = objectMapper.readValue(productionLineTemplateJsonFile.getInputStream(), ProductionLineTemplate[].class);
            for (ProductionLineTemplate productionLineTemplate: ProductionLineTemplates){
                ProductionLineTemplateHashMap.put(productionLineTemplate.getId(), productionLineTemplate);
            }

            Resource suppliersJsonFile = new ClassPathResource("JsonFiles/Suppliers.json");
            Suppliers = objectMapper.readValue(suppliersJsonFile.getInputStream(), Supplier[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Product findProductById(Integer id) {
        for (Product product : Products) {
            if (product.getId() == id)
                return product;
        }
        throw new ProductNotFoundException("Product with id: " + id + " does not exist");
    }

    public static Vehicle findVehicleByType(Enums.VehicleType vehicleType) {
        List<Vehicle> vehiclesList = Arrays.asList(Vehicles);
        return vehiclesList.stream().filter(v -> v.getVehicleType() == vehicleType).findFirst().get();
        // TODO : Exception
    }

    public static Vehicle findVehicleById(int id)
    {
        for (Vehicle vehicle : Vehicles)
        {
            if (vehicle.getId() == id)
            {
                return vehicle;
            }
        }
        return null;
    }

    public static Product[] getAllProducts() {
        return Products;
    }

    public static Factory[] getAllFactories() {
        return Factories;
    }


    public static Factory findFactoryById(Integer id) {
        for (Factory factory : Factories) {
            if (factory.getId() == id)
                return factory;
        }
        throw new FactoryNotFoundException("Factory with id : " + id + " does not exist");
    }
}