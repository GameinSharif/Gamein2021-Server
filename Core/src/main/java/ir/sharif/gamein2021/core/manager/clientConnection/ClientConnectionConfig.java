package ir.sharif.gamein2021.core.manager.clientConnection;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = {"microservice"})
@Configuration
public class ClientConnectionConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("clients.fanout");
    }
}
