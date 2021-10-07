package ir.sharif.gamein2021.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContext {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
