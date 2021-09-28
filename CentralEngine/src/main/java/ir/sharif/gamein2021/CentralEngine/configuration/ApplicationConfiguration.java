package ir.sharif.gamein2021.CentralEngine.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/config/application.properties")
public class ApplicationConfiguration {

    private final Environment environment;

    public Boolean isSingleNode;

    public ApplicationConfiguration(Environment environment) {
        this.environment = environment;
        SetConfigs();
    }

    private void SetConfigs() {
        isSingleNode = Boolean.valueOf(environment.getProperty("is-single-node"));
    }
}
