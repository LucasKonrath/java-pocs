package org.example.gsonpoc.runner;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.gsonpoc.model.Address;
import org.example.gsonpoc.model.Employee;
import org.example.gsonpoc.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@Component
public class GsonDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        basicSerialization();
        prettyPrinting();
        customTypeAdapter();
        handlingNulls();
        collectionsDemo();
        serializedNameDemo();
        nestedObjectsDemo();
        exclusionStrategyDemo();
    }

    private void basicSerialization() {
        System.out.println("\n=== Basic Serialization/Deserialization ===");
        Gson gson = new Gson();

        Person person = new Person("Alice", 30, "alice@example.com",
                List.of("reading", "coding"), LocalDate.of(1994, 5, 15));

        String json = gson.toJson(person);
        System.out.println("Serialized: " + json);

        Person deserialized = gson.fromJson(json, Person.class);
        System.out.println("Deserialized: " + deserialized);
    }

    private void prettyPrinting() {
        System.out.println("\n=== Pretty Printing ===");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Person person = new Person("Bob", 25, "bob@example.com",
                List.of("gaming", "music"), LocalDate.of(1999, 3, 20));

        System.out.println(gson.toJson(person));
    }

    private void customTypeAdapter() {
        System.out.println("\n=== Custom TypeAdapter for LocalDate ===");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        Person person = new Person("Carol", 28, "carol@example.com",
                List.of("travel"), LocalDate.of(1996, 8, 10));

        String json = gson.toJson(person);
        System.out.println("With LocalDate adapter: " + json);

        Person deserialized = gson.fromJson(json, Person.class);
        System.out.println("Deserialized birthDate: " + deserialized.getBirthDate());
    }

    private void handlingNulls() {
        System.out.println("\n=== Handling Nulls ===");
        Person person = new Person("Dave", 35, null, null, null);

        Gson defaultGson = new Gson();
        System.out.println("Default (skip nulls): " + defaultGson.toJson(person));

        Gson nullGson = new GsonBuilder().serializeNulls().create();
        System.out.println("With serializeNulls: " + nullGson.toJson(person));
    }

    private void collectionsDemo() {
        System.out.println("\n=== Collections ===");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        List<Person> people = List.of(
                new Person("Eve", 22, "eve@example.com", List.of("art"), LocalDate.of(2002, 1, 1)),
                new Person("Frank", 40, "frank@example.com", List.of("cooking", "hiking"), LocalDate.of(1984, 12, 25))
        );

        String json = gson.toJson(people);
        System.out.println("List serialized: " + json);

        Type listType = new TypeToken<List<Person>>() {}.getType();
        List<Person> deserialized = gson.fromJson(json, listType);
        System.out.println("Deserialized list size: " + deserialized.size());
        deserialized.forEach(p -> System.out.println("  - " + p.getName()));
    }

    private void serializedNameDemo() {
        System.out.println("\n=== @SerializedName ===");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        Employee employee = new Employee("Grace", 33, "grace@example.com",
                List.of("yoga"), LocalDate.of(1991, 7, 4),
                "TechCorp", new Address("123 Main St", "Springfield", "62701"), 85000);

        String json = gson.toJson(employee);
        System.out.println("Notice 'company_name' instead of 'company':");
        System.out.println(json);
    }

    private void nestedObjectsDemo() {
        System.out.println("\n=== Nested Objects ===");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        Employee employee = new Employee("Hank", 45, "hank@example.com",
                List.of("golf"), LocalDate.of(1979, 11, 30),
                "MegaCorp", new Address("456 Oak Ave", "Shelbyville", "62702"), 120000);

        String json = gson.toJson(employee);
        System.out.println(json);

        Employee deserialized = gson.fromJson(json, Employee.class);
        System.out.println("Deserialized address city: " + deserialized.getAddress().getCity());
    }

    private void exclusionStrategyDemo() {
        System.out.println("\n=== ExclusionStrategy ===");
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getName().equals("email");
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        Person person = new Person("Ivy", 27, "ivy@secret.com",
                List.of("chess"), LocalDate.of(1997, 2, 14));

        System.out.println("Email field excluded:");
        System.out.println(gson.toJson(person));
    }

    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value != null ? value.toString() : null);
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            String value = in.nextString();
            return value != null ? LocalDate.parse(value) : null;
        }
    }
}
