package ir.sharif.gamein2021.CentralEngine;


import ir.sharif.gamein2021.core.mainThread.MainThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("ir.sharif.gamein2021")
@SpringBootApplication
public class CentralEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(CentralEngineApplication.class, args);
        MainThread.main(args);
    }
}

