package ir.sharif.gamein2021.core.manager.clientHandlerConnection;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = {"microservice"})
@Configuration
public class ClientHandlerConnectionConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("clients.fanout");
    }
}
