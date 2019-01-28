package au.com.pact.demo.service;

import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;
import org.springframework.stereotype.Service;

@Service
public class DecisionServiceImpl implements DecisionService {
    @Override
    public DecisionResponse processDecision(DecisionRequest request) {
        DecisionResponse decisionResponse = new DecisionResponse();
        decisionResponse.setDecision("Approved");
        return decisionResponse;
    }
}
