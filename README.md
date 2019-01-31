# pact-workshop


### Get Started

1. Clone the project into your local:
   ```
   git clone https://github.com/Waterstrong/pact-workshop.git
   ```
2. Open terminal and cd to project directory, run command: 
   ```
   cd pact-workshop && ./gradlew cI idea
   ```
3. Open `pact-workshop.ipr` file with `IntelliJ IDEA`
   ```
   idea pact-workshop.ipr || open pact-workshop.ipr || start pact-workshop.ipr
   ```
4. Start Consumer Blue, run command in terminal 1: 
   ```
   ./gradlew :consumer-blue:bootRun
   ```
5. Start Provider Apple, run command in terminal 2:
   ```
   cd provider-apple && java -jar wiremock-standalone-*.jar --port 8081
   ```
6. Start Provider Lemon, run command in terminal 3:
   ```
   ./gradlew :provider-lemon:bootRun
   ```
7. Start Continuous Build, run command in terminal 4:
   ```
   ./gradlew build --continuous
   ```
8. Click and open [localhost swagger page](http://localhost:9000/api/swagger-ui.html) in your browser and try it out
   * Search Addresses -> Path parameter `keyword` starts with `13` or not will get different results.
   * Check Application -> Request payload `transaction` value `yes` or `no` will get different response.
   ![](images/swagger-ui.png)
   

### Pact Consumer

1. Add dependency for pact & configure the output directory of pact file

2. Add the Pact Rule to your test class to represent your provider

3. Annotate a method with Pact that returns a pact fragment for the provider and consumer

4. Annotate your test method with PactVerification and write your test inside

5. Run your test, it will generate a pact file under '/target/pacts' directory

### Pact Broker




### Pact Provider
