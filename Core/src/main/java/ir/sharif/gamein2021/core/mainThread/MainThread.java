package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class MainThread
{
    public static void main(String[] args)
    {
        ReadJsonFilesManager.ReadJsonFiles();
    }
}
