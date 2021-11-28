package ir.sharif.gamein2021.core;

import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ComponentScan(value = "ir.sharif.gamein2021.core")
@EnableAutoConfiguration
@Configuration
@EnableScheduling
public class CoreApplication
{
    public static void main(String[] args)
    {
        ReadJsonFilesManager.ReadJsonFiles();
    }
}
