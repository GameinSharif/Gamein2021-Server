package ir.sharif.gamein2021.ClientHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "ir.sharif.gamein2021.core")
public class ClientHandlerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ClientHandlerApplication.class, args);
    }
}
