package com.example.json2pojo;

import com.example.json2pojo.generated.Address;
import com.example.json2pojo.generated.Profile;
import com.example.json2pojo.generated.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Simple console application showcasing how to consume POJOs generated from JSON using jsonschema2pojo.
 */
public class App {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        App app = new App();
        User user = app.readUserFromResource();
        System.out.println(app.describeUser(user));
    }

    /**
     * Loads the sample JSON from the classpath and deserializes it into the generated {@link User} class.
     */
    public User readUserFromResource() throws IOException {
        try (InputStream input = App.class.getResourceAsStream("/json/user.json")) {
            if (input == null) {
                throw new IOException("Sample JSON '/json/user.json' could not be found on the classpath");
            }
            return objectMapper.readValue(input, User.class);
        }
    }

    /**
     * Builds a human readable summary using the generated POJO types.
     */
    public String describeUser(User user) {
        Objects.requireNonNull(user, "user");
        Profile profile = user.getProfile();
        Address address = profile != null ? profile.getAddress() : null;

        StringBuilder builder = new StringBuilder();
        builder.append("User #").append(user.getId()).append('\n');
        builder.append("  Username: ").append(user.getUsername()).append('\n');
        builder.append("  Email: ").append(user.getEmail()).append('\n');

        if (profile != null) {
            builder.append("  Display name: ").append(profile.getDisplayName()).append('\n');
            builder.append("  Birthday: ").append(profile.getBirthday()).append('\n');
        }

        if (address != null) {
            builder.append("  Address: ")
                    .append(address.getStreet()).append(", ")
                    .append(address.getCity()).append(", ")
                    .append(address.getCountry())
                    .append(" (Postal: ").append(address.getPostalCode()).append(')').append('\n');
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            builder.append("  Roles: ").append(String.join(", ", user.getRoles())).append('\n');
        }

        if (user.getPreferences() != null) {
            builder.append("  Prefers theme: ").append(user.getPreferences().getTheme()).append('\n');
            builder.append("  Newsletter opt-in: ").append(user.getPreferences().getNewsletterOptIn()).append('\n');
            builder.append("  Favorite numbers: ").append(user.getPreferences().getFavoriteNumbers()).append('\n');
        }

        return builder.toString();
    }

    /**
     * Convenience helper for tests to pull the JSON payload as a String.
     */
    public String readUserJsonAsString() throws IOException {
        try (InputStream input = App.class.getResourceAsStream("/json/user.json")) {
            if (input == null) {
                throw new IOException("Sample JSON '/json/user.json' could not be found on the classpath");
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
