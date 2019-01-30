package au.com.pact.demo.service;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.pact.demo.configuration.RestTemplateConfiguration;
import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;
import au.com.pact.demo.util.EndpointProperties;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DecisionServiceImplPactTest {
    private static final String PROVIDER_APPLE = "provider_apple";
    private static final String CONSUMER_BLUE = "consumer_blue";

    @InjectMocks
    private DecisionServiceImpl decisionService;

    @Spy
    private RestTemplateExecutor restTemplate = newRestTemplateExecutor();

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(PROVIDER_APPLE, "localhost", 8081, this);

    @Pact(provider = PROVIDER_APPLE, consumer = CONSUMER_BLUE)
    public RequestResponsePact createDecisionPact(PactDslWithProvider builder) {
        PactDslJsonBody expectedResponse = new PactDslJsonBody()
                .stringType("decision", "Rejected")
                .stringType("policyRule", "PR-8")
                .stringType("credRule", "CR-6")
                .booleanType("flag", true)
                .asBody();

        return builder
                .uponReceiving("Process Decision")
                .path("/decision")
                .method("POST")
                .willRespondWith()
                .status(200)
                .body(expectedResponse)
                .toPact();
    }

    @Test
    @PactVerification(PROVIDER_APPLE)
    public void shouldProcessDecisionGivenDecisionRequestObject() {
        DecisionRequest request = new DecisionRequest();

        DecisionResponse response = decisionService.processDecision(request);

        assertThat(response.getDecision(), is("Rejected"));
    }

    private RestTemplateExecutor newRestTemplateExecutor() {
        RestTemplateConfiguration configuration = new RestTemplateConfiguration();
        EndpointProperties properties = new EndpointProperties();
        properties.setBaseUrl("http://localhost:8081");
        return configuration.decisionApiRestTemplate(properties);
    }
}