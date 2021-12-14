package ir.sharif.gamein2021.core.manager.centralEngineConnection;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = {"microservice"})
@Configuration
public class ReceiverCentralEngineConnectionConfig {
    @Bean(value = "engineQueue")
    public Queue engineQueue() {
        return new Queue("engineQueue");
    }
}
