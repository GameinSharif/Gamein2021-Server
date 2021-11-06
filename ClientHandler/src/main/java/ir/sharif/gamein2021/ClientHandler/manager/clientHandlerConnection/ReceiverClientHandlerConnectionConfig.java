package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = {"microservice"})
@Configuration
public class ReceiverClientHandlerConnectionConfig {

    @Bean(value = "receiverQueue")
    public Queue receiverQueue() {
        return new AnonymousQueue();
    }

    @Bean(value = "engineQueue")
    public Queue engineQueue() {
        return new Queue("engineQueue");
    }

    @Bean(value = "calendarQueue")
    public Queue calendarQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding receiverBinding(FanoutExchange fanoutExchange, @Qualifier(value = "receiverQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Binding calendarBinding(FanoutExchange fanoutExchange, @Qualifier(value = "calendarQueue") Queue queue){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
