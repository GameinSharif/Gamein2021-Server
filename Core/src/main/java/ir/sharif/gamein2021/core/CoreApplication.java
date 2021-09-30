package ir.sharif.gamein2021.core;

import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication
{
    public static void main(String[] args)
    {
        //SpringApplication.run(CoreApplication.class, args);

        ReadJsonFilesManager.ReadJsonFiles();
    }
}
