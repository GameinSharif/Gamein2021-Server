package ir.sharif.gamein2021.core.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.sharif.gamein2021.core.util.Product;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Component
@Getter
public class ReadJsonFilesManager
{
    private static Product[] products;

    public static void ReadJsonFiles()
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();

            File productsJsonFile = ResourceUtils.getFile("classpath:JsonFiles/Products.json");
            products = objectMapper.readValue(productsJsonFile, Product[].class);
        }
        catch (IOException ignored)
        {
        }
    }
}