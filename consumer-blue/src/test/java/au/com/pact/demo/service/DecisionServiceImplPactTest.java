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

import static au.com.pact.demo.constant.DefaultValues.*;
import static au.com.pact.demo.util.RestTemplateExecutorBuilder.buildRestTemplate;
import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DecisionServiceImplPactTest {

    @InjectMocks
    private DecisionServiceImpl decisionService;

    @Spy
    private RestTemplateExecutor restTemplate = buildRestTemplate("http://localhost:8081");

    // TODO: Create rule PactProviderRuleMk2 to enable mock provider
    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(PROVIDER_APPLE, "localhost", 8081, this);

    // TODO: Create Decision Pact (Provider Apple <--> Consumer Blue)
    @Pact(provider = PROVIDER_APPLE, consumer = CONSUMER_BLUE)
    public RequestResponsePact createRejectedDecisionPact(PactDslWithProvider builder) {
        return builder
                .given("Transaction No")
                .uponReceiving("Process Decision")
                .path("/decision")
                .method("POST")
                .body(newRejectedRequest())
                .willRespondWith()
                .status(200)
                .headers(of("Content-Type", CONTENT_TYPE))
                .body(newRejectedResponse())
                .toPact();
    }

    @Pact(provider = PROVIDER_APPLE, consumer = CONSUMER_BLUE)
    public RequestResponsePact createApprovedDecisionPact(PactDslWithProvider builder) {
        return builder
                .given("Transaction Yes")
                .uponReceiving("Process Decision")
                .path("/decision")
                .method("POST")
                .headers(of("Content-Type", CONTENT_TYPE))
                .body(newApprovedRequest())
                .willRespondWith()
                .status(200)
                .headers(of("Content-Type", CONTENT_TYPE))
                .body(newApprovedResponse())
                .toPact();
    }

    // TODO: Create Pact Test for Provider Apple
    @Test
    @PactVerification(value = PROVIDER_APPLE, fragment = "createRejectedDecisionPact")
    public void shouldProcessDecisionToReturnRejectedGivenDecisionRequestTransactionNo() {
        DecisionRequest request = new DecisionRequest();
        request.setProfileId("id12345");
        request.setTransaction("no");

        DecisionResponse response = decisionService.processDecision(request);

        assertThat(response.getDecision(), is("Rejected"));
        assertThat(response.getPolicyRule(), is("PR-8"));
        assertThat(response.getCredRule(), is("CR-6"));
        assertThat(response.isFlag(), is(true));
    }

    @Test
    @PactVerification(value = PROVIDER_APPLE, fragment = "createApprovedDecisionPact")
    public void shouldProcessDecisionToReturnApprovedGivenDecisionRequestTransactionYes() {
        DecisionRequest request = new DecisionRequest();
        request.setProfileId("id54321");
        request.setTransaction("yes");

        DecisionResponse response = decisionService.processDecision(request);

        assertThat(response.getDecision(), is("Approved"));
    }

    private PactDslJsonBody newApprovedRequest() {
        return new PactDslJsonBody()
                .stringType("profileId")
                .stringValue("transaction", "yes")
                .asBody();
    }

    private PactDslJsonBody newApprovedResponse() {
        return new PactDslJsonBody()
                .stringType("decision", "Approved")
                .booleanType("flag", true)
                .asBody();
    }

    private PactDslJsonBody newRejectedRequest() {
        return new PactDslJsonBody()
                .stringType("profileId")
                .stringValue("transaction", "no")
                .asBody();
    }

    private PactDslJsonBody newRejectedResponse() {
        return new PactDslJsonBody()
                .stringType("decision", "Rejected")
                .stringType("policyRule", "PR-8")
                .stringType("credRule", "CR-6")
                .booleanType("flag", true)
                .asBody();
    }

}