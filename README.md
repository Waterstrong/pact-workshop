# pact-workshop
![](images/consumer-relationship.png)
![](images/pact-home-with-version.png)

### Get Started

1. Clone the project into your local:
   ```
   git clone https://github.com/Waterstrong/pact-workshop.git
   ```
2. Open terminal and cd to project directory, run command: 
   ```
   cd pact-workshop; ./gradlew idea
   ```
3. Open `pact-workshop.ipr` file with `IntelliJ IDEA`
   ```
   idea pact-workshop.ipr || open pact-workshop.ipr || start pact-workshop.ipr
   ```
4. Start Consumer Blue, run command in terminal 1: 
   ```
   ./gradlew :consumer-blue:bootRun
   ```
   Debug mode parameter: `--debug-jvm`.
5. Start Provider Apple, run command in terminal 2:
   ```
   cd provider-apple; java -jar wiremock-standalone-*.jar --port 8081 --verbose
   ```
6. Start Provider Lemon, run command in terminal 3:
   ```
   ./gradlew :provider-lemon:bootRun
   ```
7. Start Continuous Build, run command in terminal 4:
   ```
   ./gradlew build --continuous
   ```
8. Click and open [localhost swagger page](http://localhost:9000/api/swagger-ui.html) in your browser and try it out:
   * Search Addresses -> Path parameter `keyword` starts with `13` or not will get different results.
   * Check Application -> Request payload `transaction` value `yes` or `no` will get different response.
   ![](images/swagger-ui.png)
   

----
### Build Pact Consumer Steps

1. Add dependency for pact consumer at line 13 in `consumer-blue/build.gradle` file:
   ```
   testImplementation 'au.com.dius:pact-jvm-consumer-junit_2.12:3.6.1'
   ```
2. Run `./gradlew idea` to download the new dependencies.
3. Create rule `PactProviderRuleMk2` to enable mock provider in `DecisionServiceImplPactTest.java`:
   ```
   @Rule
   public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(PROVIDER_APPLE, "localhost", 8081, this);
   ```
   Notes: use `PactHttpsProviderRuleMk2` to mock provider on `https` protocol.
4. Create Decision Pact (Provider Apple <--> Consumer Blue) in `DecisionServiceImplPactTest.java`:
   ```
   @Pact(provider = PROVIDER_APPLE, consumer = CONSUMER_BLUE)
   public RequestResponsePact createDecisionPact(PactDslWithProvider builder) {
       PactDslJsonBody expectedResponse = new PactDslJsonBody()
               .stringType("decision", "Rejected")
               .stringType("policyRule", "PR-8")
               .stringType("credRule", "CR-6")
               .booleanType("flag", true)
               .asBody();

       PactDslJsonBody request = new PactDslJsonBody()
               .stringType("profileId")
               .stringValue("transaction", "no")
               .asBody();

       return builder
               .uponReceiving("Process Decision")
               .path("/decision")
               .body(request)
               .method("POST")
               .willRespondWith()
               .status(200)
               .body(expectedResponse)
               .toPact();
   }   
   ```
5. Create Pact Test with `PactVerification` for Provider Apple in `DecisionServiceImplPactTest.java`:
   ```
   @Test
   @PactVerification(PROVIDER_APPLE)
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
   ```
6. Run the tests, it will generate a pact file under `target/pacts/` default directory. It looks like:
   ``` consumer_blue-provider_apple.json
   {
       "provider": { "name": "provider_apple" },
       "consumer": { "name": "consumer_blue" },
       "interactions": [
           {
               "description": "Process Decision",
               "request": {
                   "method": "POST", "path": "/decision",
                   "body": { ... },
                   "matchingRules": { ... },
                   ...
               },
               "response": {
                   "status": 200,
                   "body": { ... },
                   "matchingRules": {
                       "body": { ... },
                       "header": { ...}
                   },
                   ...
               },
               "providerStates": [ ... ]
           }
       ],
       "metadata": { ... }
   }
   ```
   The Pact file directory can be overwritten with the `pact.rootDir` system property. This property needs to be set on the test JVM as most build tools will fork a new JVM to run the tests.
   ```
   test {
       systemProperties['pact.rootDir'] = "$buildDir/pacts"
   }
   ```

7. Do one more example to practice with similar above steps in `AddressServiceImplPactTest.java`:
   ```
   @Rule
   public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2(PROVIDER_LEMON, "localhost", 8082, this);

   @Pact(provider = PROVIDER_LEMON, consumer = CONSUMER_BLUE)
   public RequestResponsePact createAddressPact(PactDslWithProvider builder) {
       PactDslJsonBody expectedResponse = new PactDslJsonBody()
               .array("addresses")
               .stringType("1304/7 Riverside Quay, VIC 3006")
               .stringType("1305/8 Riverside Quay, VIC 3006")
               .closeArray()
               .asBody();

       return builder
               .uponReceiving("Search Addresses")
               .path("/addresses")
               .query("keyword=13 Riverside")
               .method("GET")
               .willRespondWith()
               .status(200)
               .body(expectedResponse)
               .toPact();
   }

   @Test
   @PactVerification(PROVIDER_LEMON)
   public void shouldSearchAddressesGivenAddressKeyword() {
       AddressResponse addressResponse = addressService.searchAddresses("13 Riverside");
       assertThat(addressResponse.getAddresses(), hasSize(2));
       assertThat(addressResponse.getAddresses().get(0), is("1304/7 Riverside Quay, VIC 3006"));
   }
   ```

----
### Setup Pact Broker and publish Pact files

** How to setup Pact Broker service ?**

There are few options to setup Pact Broker: e.g. [Hosted Pact Broker](http://pact.dius.com.au/), [Pact Broker with Ruby](pact-broker-ruby/README.md), [Terraform on AWS](https://github.com/nadnerb/terraform-pact-broker), [Pact Broker Openshift](https://github.com/jaimeniswonger/pact-broker-openshift) and [Pact Broker Docker container](https://hub.docker.com/r/dius/pact-broker/). Choose one of the options to try.

For this workshop, we'd like to use docker container solution to setup Pact Broker. For more instructions, refer to [Dockerised Pact Broker](https://github.com/DiUS/pact_broker-docker) and [Pact Foundation Pact Broker](https://github.com/pact-foundation/pact_broker).

If your team is using AWS, the Dockerised Pact Broker with AWS RDS Postgres database solution is recommended.

For this workshop demo, the [docker-compose.yml](pact-broker/docker-compose.yml) is ready for quick start-up.
```
cd pact-broker; docker-compose up
```

Check if Pact Broker service is up running at both `http` and `https` protocols: [http://localhost/](http://localhost/), [https://localhost:8443/](https://localhost:8443/). 

The username and password are `readonly:password`(developer readonly) and `pactuser:password`(pipeline publish)

**How to publish consumer Pact files to Pact Broker ?**

1. Add Pact plugin in `consumer-blue/build.gradle` file:
   ```
   plugins {
       id "au.com.dius.pact" version "3.6.1"
   }
   ``` 
2. Add Gradle task `pactPublish` in the same gradle file with auth info (using http as demo here):
   ```
   pact {
       publish {
           pactDirectory = './target/pacts'
           pactBrokerUrl = 'http://pactuser:password@localhost'
       }
   
   }
   ``` 
   Notes: username and password should be configured as environment variable from pipeline.
3. Run publish task in terminal:
   ```
   ./gradlew :consumer-blue:pactPublish
   ```
4. Refresh the Pact Broker endpoint and see the Pact file with versions and relationships:
   ![](images/pact-home-without-version.png)
   
---
### Build Pact Provider
##### Option 1: Pact JVM Provider Gradle 
1. Add Pact plugin in `provider-lemon/build.gradle` file:
   ```
   plugins {
       id "au.com.dius.pact" version "3.6.1"
   }
   ```
2. Add Pact jvm provider dependency
   ```
   testImplementation 'au.com.dius:pact-jvm-provider_2.12:3.6.1'
   ```
3. Run `./gradlew idea` to download the latest dependencies.
4. Define the pacts between consumers and providers
   ```
   pact {
       serviceProviders {
           provider_lemon {
               protocol = 'http'
               host = 'localhost'
               port = 8082
               path = '/api'
               hasPactsFromPactBroker('http://localhost', authentication: ['Basic', 'pactuser', 'password'])
           }
       }
   }
   ```
   Notes: More options(Local files, Runtime, S3 bucket, Pact Broker) to verify pact files, refer to page [pact-jvm-provider-gradle](https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-provider-gradle). 
5. Run pact verify for provider 
   ```
   ./gradlew :provider-lemon:pactVerify
   ```
   ![](images/verify-provider-lemon.png)
6. As you will get the warning message `Skipping publishing of verification results as it has been disabled (pact.verifier.publishResults is not 'true')`.
   To fix it by adding the gradle parameter `-Ppact.verifier.publishResults=true` or add `pact.verifier.publishResults=true` in `gradle.properties`. 
   Then it will update the verify status in Pact Broker.

##### Option 2: Pact Provider Verification
[Pact Provider Verification](https://github.com/pact-foundation/pact-provider-verifier) - This setup simplifies Pact Provider verification process in any language, wrapping the Ruby implementation into a cross-platform, binary-like CLI tool.
It seems that the CLI is not parsing matcher rules correctly. The pactVerify task can also be used on consumer side.

1. Make sure the Provider Apple Wiremock service is running. To refresh the wiremock mapping data by running command:
   ```
   curl -X POST http://localhost:8081/__admin/mappings/reset --verbose
   ``` 
2. Install Ruby, then install `pact-provider-verifier` CLI:
   ```
   gem install pact-provider-verifier
   ```
3. Run the following command to verify Provider Apple:
   ```
   pact-provider-verifier --pact-broker-base-url=http://localhost --broker-username=pactuser --broker-password=password --provider-base-url=http://localhost:8081/api --provider=provider_apple --provider-app-version=1.0.0 --publish-verification-results=true --verbose
   ```
   ![](images/verify-provider-apple.png)
   ![](images/pact-interactions-verified.png)

##### Options 3, 4, 5...
* [Pact Docs](https://docs.pact.io/implementation_guides)
* [Verifying Pacts](https://docs.pact.io/implementation_guides/ruby/verifying_pacts)
* [Pact Provider Verification in Docker](https://github.com/DiUS/pact-provider-verifier-docker)


---
### More tips
1. Define Provider state using `.given()`
   * [AddressServiceImplPactTest.java](https://github.com/Waterstrong/pact-workshop/blob/completed/consumer-blue/src/test/java/au/com/pact/demo/service/AddressServiceImplPactTest.java)
   * [DecisionServiceImplPactTest.java](https://github.com/Waterstrong/pact-workshop/blob/completed/consumer-blue/src/test/java/au/com/pact/demo/service/DecisionServiceImplPactTest.java)
2. Verify the provider with unit test
   * [Pact junit runner](https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-provider-junit#download-pacts-from-a-pact-broker) - Library provides ability to play contract tests against a provider service in JUnit fashionable way.
3. Try heart beat and badge endpoint
   ```
   http://localhost/diagnostic/status/heartbeat
   https://localhost:8443/diagnostic/status/heartbeat
   
   http://localhost/pacts/provider/provider_lemon/consumer/consumer_blue/latest/badge.svg
   https://localhost:8443/pacts/provider/provider_lemon/consumer/consumer_blue/latest/badge.svg
   ```
   ![](images/pact-badge.png)
4. Define Pact file version
   ```
   version = "1.0.0"  // In xxx/build.gradle or within pact block. It will use version of the gradle project by default.   
   ```
   ![](images/pact-consumer-provider-versions.png)
4. Stay on `master` branch to practice workshop step by step with instructions. Switch to `completed` branch to get completed code demo.
5. Feel free to ask questions, share thoughts and give feedback :)

---
### Self-reflection Questions
- [ ] Can I explain the CDC(Consumer Driven Contract Testing) ? 
- [ ] Can I explain the differences between contract test and functional test ?
- [ ] Do I know one or two tools for contract testing ?
- [ ] Do I know how to write script to generate Pact file?
- [ ] Do I know how to verify provider in Pact ?
- [ ] Do I know what is Pact Broker ?
- [ ] Do I know how to publish the Pact file ?
- [ ] Do I know the best practices for Contract Testing?

---
### More Readings
* [Pact Docs - docs.pact.io](https://docs.pact.io/) 
* [Closing the loop with Pact verifications](https://dius.com.au/2018/01/21/closing-the-loop-with-pact-verifications/)
* [Trust but verify. Using Pact for contract testing](https://devblog.xero.com/trust-but-verify-using-pact-for-contract-testing-495a1e303a6)
* [Consumer-Driven Contracts using a Pact broker](https://medium.com/techbeatscorner/consumer-driven-contracts-using-a-pact-broker-b1743c2f8fe5)
* [Advanced Contract Testing - Pact Verification with Pattern Matching](https://blog.risingstack.com/advanced-contract-testing-pact-verification-with-pattern-matching/)
* [Consumer Driven Contract Tests](https://www.bbva.com/en/consumer-driven-contract-tests/) 
* [Contract Tests vs Functional Tests](https://docs.pact.io/best_practices/consumer/contract_tests_not_functional_tests)
* [Consumer-Driven Contracts: A Service Evolution Pattern](https://martinfowler.com/articles/consumerDrivenContracts.html) 
* [Understanding Contract Testing for Microservices](https://www.mabl.com/blog/understanding-contract-testing-microservices-mabl)