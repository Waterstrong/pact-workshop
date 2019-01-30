package au.com.pact.demo.model;

public class DecisionResponse {
    private String decision;
    private String policyRule;
    private String credRule;
    private boolean flag;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getPolicyRule() {
        return policyRule;
    }

    public void setPolicyRule(String policyRule) {
        this.policyRule = policyRule;
    }

    public String getCredRule() {
        return credRule;
    }

    public void setCredRule(String credRule) {
        this.credRule = credRule;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
