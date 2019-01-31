# pact-workshop

### Get Started

1. Clone the project into your local
2. Open terminal and cd to project directory, run `./gradlew cI idea`
3. Open `pact-workshop.ipr` file with `IntelliJ IDEA`
4. Run Consumer Blue: type command `./gradlew :consumer-blue:bootRun` in terminal
5. Run Consumer Apple: type command `java -jar wiremock-standalone-*.jar --port 8081` in terminal
6. Run Provider Lemon: type command `./gradlew :provider-lemon:bootRun` in terminals
7. Start Continuous Build: type command `./gradlew build --continuous` in terminals
8. Open [http://localhost:8081/api/swagger-ui.html](http://localhost:8081/api/swagger-ui.html) in your browser and hint the endpoint

### Pact Consumer

1. Add dependency for pact & configure the output directory of pact file

2. Add the Pact Rule to your test class to represent your provider

3. Annotate a method with Pact that returns a pact fragment for the provider and consumer

4. Annotate your test method with PactVerification and write your test inside

5. Run your test, it will generate a pact file under '/target/pacts' directory

### Pact Broker




### Pact Provider
