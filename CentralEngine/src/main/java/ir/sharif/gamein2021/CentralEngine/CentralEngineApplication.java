package ir.sharif.gamein2021.CentralEngine;


import co.elastic.apm.opentracing.ElasticApmTracer;
import io.opentracing.Tracer;
import ir.sharif.gamein2021.core.mainThread.MainThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("ir.sharif.gamein2021")
@SpringBootApplication
public class CentralEngineApplication {
    public static void main(String[] args) {
        Tracer tracer = new ElasticApmTracer();
        SpringApplication.run(CentralEngineApplication.class, args);
        MainThread.main(args);
    }
}

