# pact-workshop
![](images/consumer-relationship.png)

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
               }
           }
       ],
       "metadata": { ... }
   }
   ```
   The Pact file directory can be changed by overriding `PACT_DIR="<new-directory>"` in `build.gradle` `ext{}` block.

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
   ![](images/pact-files.png)
   
---
### Build Pact Provider
##### Option1: Pact JVM Provider Gradle 
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
