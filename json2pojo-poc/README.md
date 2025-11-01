# json2pojo Proof of Concept

This proof of concept shows how to convert raw JSON files into strongly-typed Java POJOs by wiring the [`jsonschema2pojo`](https://www.jsonschema2pojo.org/) code generator into a Maven build. Although the plugin is called *jsonschema2pojo*, it can also ingest plain JSON samples when `sourceType` is set to `json`, which makes it a good stand-in for a "json2pojo" workflow.

## What it demonstrates

- How to configure the `jsonschema2pojo-maven-plugin` to generate Java classes during the `generate-sources` phase.
- How to keep JSON samples in `src/main/resources/json` and turn them into Java classes inside `com.example.json2pojo.generated`.
- How to consume the generated POJOs with Jackson in application code (`App.java`).
- An executable example (`App.main`) and a JUnit test (`AppTest`) that prove the generated types work as expected.

## Project layout

```
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/json2pojo/App.java
│   │   └── resources/json/user.json
│   └── test
│       └── java/com/example/json2pojo/AppTest.java
```

During the build, the plugin writes generated classes to `target/generated-sources/json2pojo`, which Maven then adds to the compilation classpath.

## Prerequisites

- Java 17+
- Maven 3.9+

## Running the demo

```bash
mvn clean verify
```

The command will:

1. Generate POJOs from `src/main/resources/json/user.json`.
2. Compile the application alongside the generated sources.
3. Run the `AppTest` to assert the mapping works.

To run the console app and print the formatted summary:

```bash
mvn -q exec:java -Dexec.mainClass=com.example.json2pojo.App
```

## Exploring the generated source

After running `mvn generate-sources` (or any goal that reaches that phase), inspect the generated POJOs:

```bash
ls target/generated-sources/json2pojo/com/example/json2pojo/generated
```

You should see classes such as `User.java`, `Profile.java`, `Address.java`, and `Preferences.java`, created automatically from the input JSON.

## Extending the POC

- Add more JSON samples to `src/main/resources/json` to generate additional models.
- Switch the `annotationStyle` to `gson`, `jackson1`, or `none` depending on the JSON library you prefer.
- Provide JSON Schema documents instead of concrete JSON to get stronger type guarantees (set `sourceType` back to `jsonschema`).
