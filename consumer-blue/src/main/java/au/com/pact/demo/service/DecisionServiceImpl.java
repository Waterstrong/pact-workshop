package au.com.pact.demo.service;

import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;
import au.com.pact.demo.util.RestTemplateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DecisionServiceImpl implements DecisionService {
    @Autowired
    @Qualifier("decisionApiRestTemplate")
    private RestTemplateExecutor restTemplate;

    @Override
    public DecisionResponse processDecision(DecisionRequest request) {
        return restTemplate.postForObject("/decision", request, DecisionResponse.class);
    }
}
