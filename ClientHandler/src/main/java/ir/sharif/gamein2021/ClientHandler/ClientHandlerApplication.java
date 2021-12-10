package ir.sharif.gamein2021.ClientHandler;


import ir.sharif.gamein2021.core.mainThread.MainThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan("ir.sharif.gamein2021")
@SpringBootApplication
public class ClientHandlerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ClientHandlerApplication.class, args);

        Environment environment = context.getEnvironment();
        String[] profiles = environment.getActiveProfiles();
//        if (Arrays.stream(profiles).noneMatch(x -> x.equals("microservice")))
//        {
        MainThread.main(args);
//        }
    }
}
