package ir.sharif.gamein2021.ClientHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("ir.sharif.gamein2021.core")
@SpringBootApplication(scanBasePackages = {"ir.sharif.gamein2021"})
public class ClientHandlerApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication.run(ClientHandlerApplication.class, args);
    }
}
