package org.example.hypersqlpoc.config;

import org.example.hypersqlpoc.entity.User;
import org.example.hypersqlpoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User("john_doe", "john.doe@example.com", "John", "Doe"));
            userRepository.save(new User("jane_smith", "jane.smith@example.com", "Jane", "Smith"));
            userRepository.save(new User("bob_johnson", "bob.johnson@example.com", "Bob", "Johnson"));
            userRepository.save(new User("alice_wilson", "alice.wilson@example.com", "Alice", "Wilson"));
            userRepository.save(new User("charlie_brown", "charlie.brown@example.com", "Charlie", "Brown"));

            System.out.println("Sample data initialized successfully!");
        }
    }
}
