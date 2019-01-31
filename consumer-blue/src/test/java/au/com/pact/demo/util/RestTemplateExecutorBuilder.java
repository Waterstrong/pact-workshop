package au.com.pact.demo.util;

import au.com.pact.demo.configuration.RestTemplateConfiguration;

public class RestTemplateExecutorBuilder {
    public static RestTemplateExecutor buildRestTemplate(String baseUrl) {
        RestTemplateConfiguration configuration = new RestTemplateConfiguration();
        EndpointProperties properties = new EndpointProperties();
        properties.setBaseUrl(baseUrl);
        return configuration.decisionApiRestTemplate(properties);
    }

}
