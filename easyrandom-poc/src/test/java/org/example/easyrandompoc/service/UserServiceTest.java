package org.example.easyrandompoc.service;

import org.example.easyrandompoc.model.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        userService = new UserService();

        // Configure EasyRandom with custom parameters
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(123L)
                .objectPoolSize(100)
                .randomizationDepth(3)
                .stringLengthRange(5, 50)
                .collectionSizeRange(1, 10);

        easyRandom = new EasyRandom(parameters);
    }

    @Test
    @DisplayName("Should return false for null user when checking if adult")
    void isAdult_ShouldReturnFalse_WhenUserIsNull() {
        assertFalse(userService.isAdult(null));
    }

    @Test
    @DisplayName("Should return false when birth date is null")
    void isAdult_ShouldReturnFalse_WhenBirthDateIsNull() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(null);

        assertFalse(userService.isAdult(user));
    }

    @Test
    @DisplayName("Should return true for adult user")
    void isAdult_ShouldReturnTrue_WhenUserIsAdult() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(25));

        assertTrue(userService.isAdult(user));
    }

    @Test
    @DisplayName("Should return false for minor user")
    void isAdult_ShouldReturnFalse_WhenUserIsMinor() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(16));

        assertFalse(userService.isAdult(user));
    }

    @RepeatedTest(10)
    @DisplayName("Should consistently handle random adult users")
    void isAdult_ShouldHandleRandomAdultUsers() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(20 + (int)(Math.random() * 30))); // 20-50 years old

        assertTrue(userService.isAdult(user));
    }

    @Test
    @DisplayName("Should return false for null user when validating email")
    void isValidEmail_ShouldReturnFalse_WhenUserIsNull() {
        assertFalse(userService.isValidEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user@domain.org", "admin@company.co.uk"})
    @DisplayName("Should return true for valid email formats")
    void isValidEmail_ShouldReturnTrue_ForValidEmails(String email) {
        User user = easyRandom.nextObject(User.class);
        user.setEmail(email);

        assertTrue(userService.isValidEmail(user));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "test@", "@domain.com", "test.com", "a@b", ""})
    @DisplayName("Should return false for invalid email formats")
    void isValidEmail_ShouldReturnFalse_ForInvalidEmails(String email) {
        User user = easyRandom.nextObject(User.class);
        user.setEmail(email);

        assertFalse(userService.isValidEmail(user));
    }

    @Test
    @DisplayName("Should return false when user cannot be activated")
    void canActivateUser_ShouldReturnFalse_WhenUserCannotBeActivated() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(16)); // Minor
        user.setEmail("invalid-email");
        user.setName("");

        assertFalse(userService.canActivateUser(user));
    }

    @Test
    @DisplayName("Should return true when user can be activated")
    void canActivateUser_ShouldReturnTrue_WhenUserCanBeActivated() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(25)); // Adult
        user.setEmail("valid@email.com");
        user.setName("Valid Name");

        assertTrue(userService.canActivateUser(user));
    }

    @Test
    @DisplayName("Should return MINOR category for users under 18")
    void getUserCategory_ShouldReturnMinor_ForUsersUnder18() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(16));

        assertEquals("MINOR", userService.getUserCategory(user));
    }

    @Test
    @DisplayName("Should return ADULT category for users 18-64")
    void getUserCategory_ShouldReturnAdult_ForUsersInAdultRange() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(35));

        assertEquals("ADULT", userService.getUserCategory(user));
    }

    @Test
    @DisplayName("Should return SENIOR category for users 65+")
    void getUserCategory_ShouldReturnSenior_ForUsersOver65() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(70));

        assertEquals("SENIOR", userService.getUserCategory(user));
    }

    @Test
    @DisplayName("Should return UNKNOWN category for null user")
    void getUserCategory_ShouldReturnUnknown_ForNullUser() {
        assertEquals("UNKNOWN", userService.getUserCategory(null));
    }

    @Test
    @DisplayName("Should calculate discount correctly for senior users")
    void calculateDiscount_ShouldReturn15Percent_ForSeniorUsers() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(70));
        user.setActive(true);

        assertEquals(0.15, userService.calculateDiscount(user), 0.001);
    }

    @Test
    @DisplayName("Should calculate discount correctly for adult users")
    void calculateDiscount_ShouldReturn5Percent_ForAdultUsers() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(35));
        user.setActive(true);

        assertEquals(0.05, userService.calculateDiscount(user), 0.001);
    }

    @Test
    @DisplayName("Should return zero discount for inactive users")
    void calculateDiscount_ShouldReturnZero_ForInactiveUsers() {
        User user = easyRandom.nextObject(User.class);
        user.setBirthDate(LocalDate.now().minusYears(35));
        user.setActive(false);

        assertEquals(0.0, userService.calculateDiscount(user), 0.001);
    }

    @RepeatedTest(20)
    @DisplayName("Should handle random users consistently")
    void calculateDiscount_ShouldHandleRandomUsersConsistently() {
        User user = easyRandom.nextObject(User.class);
        user.setActive(true);

        double discount = userService.calculateDiscount(user);

        assertTrue(discount >= 0.0 && discount <= 0.15,
                "Discount should be between 0.0 and 0.15, but was: " + discount);
    }
}
