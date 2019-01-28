# pact-workshop

### Get Started

1. Clone the project into your local
2. Open terminal and cd to project directory, run `./gradlew idea`
3. Open `pact-workshop.ipr` file with `IntelliJ IDEA`
4. Type command `./gradlew bootRun` and `./gradlew build --continuous` in separate terminals
5. Open [http://localhost:8081/api/swagger-ui.html](http://localhost:8081/api/swagger-ui.html) in your browser

### Pact Consumer

1. Add dependency for pact & configure the output directory of pact file

2. Add the Pact Rule to your test class to represent your provider

3. Annotate a method with Pact that returns a pact fragment for the provider and consumer

4. Annotate your test method with PactVerification and write your test inside

5. Run your test, it will generate a pact file under '/target/pacts' directory

### Pact Broker




### Pact Provider
