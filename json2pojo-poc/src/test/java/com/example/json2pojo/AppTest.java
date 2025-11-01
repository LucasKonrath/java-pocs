package com.example.json2pojo;

import com.example.json2pojo.generated.Address;
import com.example.json2pojo.generated.Profile;
import com.example.json2pojo.generated.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class AppTest {

    @Test
    @DisplayName("Generated POJOs map the sample JSON payload")
    void shouldDeserializeSampleJson() throws Exception {
        App app = new App();

        User user = app.readUserFromResource();

        assertNotNull(user, "user must not be null");
        assertEquals(123L, user.getId());
        assertEquals("luna", user.getUsername());
        assertEquals("luna@example.com", user.getEmail());

        Profile profile = user.getProfile();
        assertNotNull(profile, "profile must be present");
        assertEquals("Luna D.", profile.getDisplayName());
        assertEquals("1993-05-14", profile.getBirthday());

        Address address = profile.getAddress();
        assertNotNull(address, "address must be present");
        assertEquals("42 Galaxy Way", address.getStreet());
        assertEquals("São Paulo", address.getCity());
        assertEquals("Brazil", address.getCountry());
        assertEquals("01234-567", address.getPostalCode());

    assertIterableEquals(
        java.util.List.of("ADMIN", "USER"),
        user.getRoles(),
        "roles should match sample payload"
    );

    assertIterableEquals(
        java.util.List.of(3L, 7L, 21L),
        user.getPreferences().getFavoriteNumbers(),
        "favorite numbers should match sample payload"
    );
    }
}
