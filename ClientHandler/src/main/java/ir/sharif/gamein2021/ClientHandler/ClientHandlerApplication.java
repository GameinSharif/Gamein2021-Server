package ir.sharif.gamein2021.ClientHandler;

import ir.sharif.gamein2021.core.CoreApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("ir.sharif.gamein2021")
@SpringBootApplication
public class ClientHandlerApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication.run(ClientHandlerApplication.class, args);

        CoreApplication.main(args);
    }
}
