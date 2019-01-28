package au.com.pact.demo.service;

import au.com.pact.demo.model.DecisionRequest;
import au.com.pact.demo.model.DecisionResponse;

public interface DecisionService {
    DecisionResponse processDecision(DecisionRequest request);
}
