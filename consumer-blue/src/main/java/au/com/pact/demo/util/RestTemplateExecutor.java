package au.com.pact.demo.util;

import org.springframework.web.client.RestTemplate;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class RestTemplateExecutor {
    private static final String URL_TEMPLATE = "{baseUrl}{pathParameters}";
    private RestTemplate restTemplate;
    private EndpointProperties endpointProperties;

    public RestTemplateExecutor(RestTemplate restTemplate, EndpointProperties endpointProperties) {
        this.restTemplate = restTemplate;
        this.endpointProperties = endpointProperties;
    }

    public <T> T postForObject(String path, Object request, Class<T> responseType) {
        String url = fromPath(URL_TEMPLATE).buildAndExpand(endpointProperties.getBaseUrl(), path).toString();

        return restTemplate.postForObject(url, request, responseType);
    }

    public <T> T getForObject(String pathParameters, Class<T> responseType) {
        String url = fromPath(URL_TEMPLATE).buildAndExpand(endpointProperties.getBaseUrl(), pathParameters).toString();

        return restTemplate.getForObject(url, responseType);
    }

}
