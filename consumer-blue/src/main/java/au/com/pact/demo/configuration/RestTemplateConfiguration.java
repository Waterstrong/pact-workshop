package au.com.pact.demo.configuration;

import au.com.pact.demo.util.EndpointProperties;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "decision.api")
    public EndpointProperties decisionApiProperties() {
        return new EndpointProperties();
    }

    @Bean
    public RestTemplateExecutor decisionApiRestTemplate(EndpointProperties decisionApiProperties) {
        return new RestTemplateExecutor(new RestTemplateBuilder().build(), decisionApiProperties);
    }

    @Bean
    @ConfigurationProperties(prefix = "address.api")
    public EndpointProperties addressApiProperties() {
        return new EndpointProperties();
    }

    @Bean
    public RestTemplateExecutor addressApiRestTemplate(EndpointProperties addressApiProperties) {
        return new RestTemplateExecutor(new RestTemplateBuilder().build(), addressApiProperties);
    }
}
