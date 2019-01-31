package au.com.pact.demo.service;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static au.com.pact.demo.constant.DefaultValues.CONSUMER_BLUE;
import static au.com.pact.demo.constant.DefaultValues.PROVIDER_APPLE;
import static au.com.pact.demo.util.RestTemplateExecutorBuilder.buildRestTemplate;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DecisionServiceImplPactTest {

    @InjectMocks
    private DecisionServiceImpl decisionService;

    @Spy
    private RestTemplateExecutor restTemplate = buildRestTemplate("http://localhost:8081");

    // TODO: Create rule PactProviderRuleMk2 to enable mock provider


    // TODO: Create Decision Pact (Provider Apple <--> Consumer Blue)


    // TODO: Create Pact Test for Provider Apple


}