package org.example.hypersqlpoc;

import org.example.hypersqlpoc.entity.User;
import org.example.hypersqlpoc.repository.UserRepository;
import org.example.hypersqlpoc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:hsqldb:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class HypersqlPocApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        assertNotNull(userRepository);
        assertNotNull(userService);
    }

    @Test
    void testUserCreationAndRetrieval() {
        User user = new User("testuser", "test@example.com", "Test", "User");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());

        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("testuser", retrievedUser.get().getUsername());
    }

    @Test
    void testUserRepositoryQueries() {
        User user1 = new User("uniquetest1", "unique1@test.com", "UniqueAlice", "TestJohnson");
        User user2 = new User("uniquetest2", "unique2@test.com", "UniqueBob", "TestJohnson");
        User user3 = new User("uniquetest3", "unique3@test.com", "UniqueCharlie", "TestSmith");

        userRepository.saveAll(List.of(user1, user2, user3));

        Optional<User> foundUser = userRepository.findByUsername("uniquetest1");
        assertTrue(foundUser.isPresent());
        assertEquals("UniqueAlice", foundUser.get().getFirstName());

        Optional<User> foundByEmail = userRepository.findByEmail("unique2@test.com");
        assertTrue(foundByEmail.isPresent());
        assertEquals("UniqueBob", foundByEmail.get().getFirstName());

        List<User> aliceUsers = userRepository.findByFirstNameContainingIgnoreCase("uniquealice");
        assertEquals(1, aliceUsers.size());

        List<User> johnsonUsers = userRepository.findByLastNameContainingIgnoreCase("testjohnson");
        assertEquals(2, johnsonUsers.size());

        long userCount = userRepository.countAllUsers();
        assertTrue(userCount >= 3);
    }

    @Test
    void testUserService() {
        User newUser = userService.createUser("servicetest", "service@test.com", "Service", "Test");
        assertNotNull(newUser.getId());
        assertEquals("servicetest", newUser.getUsername());

        List<User> allUsers = userService.getAllUsers();
        assertTrue(allUsers.size() > 0);

        newUser.setFirstName("UpdatedService");
        User updatedUser = userService.updateUser(newUser.getId(), newUser);
        assertEquals("UpdatedService", updatedUser.getFirstName());

        List<User> searchResults = userService.searchUsersByFirstName("UpdatedService");
        assertEquals(1, searchResults.size());

        long count = userService.getUserCount();
        assertTrue(count > 0);

        Long userId = newUser.getId();
        userService.deleteUser(userId);
        Optional<User> deletedUser = userService.getUserById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testHyperSQLSpecificFeatures() {
        User user = new User("hypersql_test", "hypersql@test.com", "HyperSQL", "Test");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertTrue(savedUser.getId() > 0);

        assertNotNull(savedUser.getCreatedAt());

        long initialCount = userRepository.count();

        userRepository.save(new User("temp1", "temp1@test.com", "Temp", "One"));
        userRepository.save(new User("temp2", "temp2@test.com", "Temp", "Two"));

        long afterInsertCount = userRepository.count();
        assertEquals(initialCount + 2, afterInsertCount);
    }
}
