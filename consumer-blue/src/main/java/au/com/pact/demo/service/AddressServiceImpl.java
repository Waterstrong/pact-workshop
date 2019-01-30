package au.com.pact.demo.service;

import au.com.pact.demo.model.AddressResponse;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.catalina.util.URLEncoder.DEFAULT;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    @Qualifier("addressApiRestTemplate")
    private RestTemplateExecutor restTemplate;

    @Override
    public AddressResponse searchAddresses(String keyword) {
        String pathParameters = "/addresses?keyword=" + DEFAULT.encode(keyword, defaultCharset());

        return restTemplate.getForObject(pathParameters, AddressResponse.class);
    }
}
