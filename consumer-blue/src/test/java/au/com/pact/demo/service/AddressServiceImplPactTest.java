package au.com.pact.demo.service;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.pact.demo.model.AddressResponse;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static au.com.pact.demo.constant.DefaultValues.CONSUMER_BLUE;
import static au.com.pact.demo.constant.DefaultValues.PROVIDER_LEMON;
import static au.com.pact.demo.util.RestTemplateExecutorBuilder.buildRestTemplate;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceImplPactTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Spy
    private RestTemplateExecutor restTemplate = buildRestTemplate("http://localhost:8082");

    // TODO: Create rule PactProviderRuleMk2 to enable mock provider


    // TODO: Create Address Pact (Provider Lemon <--> Consumer Blue)


    // TODO: Create Pact Test for Provider Lemon


}