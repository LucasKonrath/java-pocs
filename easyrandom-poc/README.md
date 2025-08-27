# EasyRandom Mutation Testing POC

This project demonstrates how to use **EasyRandom** with **JUnit 5** for comprehensive mutation testing in Java applications.

## Overview

Mutation testing is a powerful technique that evaluates the quality of your test suite by introducing small changes (mutations) to your code and checking if your tests can detect these changes. This POC shows how EasyRandom can generate diverse test data to improve mutation test coverage.

## Technologies Used

- **Java 24**
- **Spring Boot 3.5.5**
- **JUnit 5** - Testing framework
- **EasyRandom 5.0.0** - Test data generation
- **PIT (PITest)** - Mutation testing framework
- **Gradle** - Build tool

## Project Structure

```
src/main/java/org/example/easyrandompoc/
├── EasyrandomPocApplication.java
├── model/
│   ├── User.java              # User domain model
│   ├── Order.java             # Order domain model
│   ├── OrderItem.java         # Order item model
│   └── OrderStatus.java       # Order status enum
└── service/
    ├── UserService.java       # User business logic
    └── OrderService.java      # Order business logic

src/test/java/org/example/easyrandompoc/service/
├── UserServiceTest.java       # Comprehensive user service tests
├── OrderServiceTest.java      # Comprehensive order service tests
└── AdvancedEasyRandomTest.java # Property-based testing examples
```

## Key Features Demonstrated

### 1. EasyRandom Configuration
- Custom randomization parameters
- Field-specific randomizers
- Exclusion of specific fields
- Seed-based reproducible tests

### 2. Property-Based Testing
- Invariant testing with random data
- Business rule validation across many inputs
- Bulk data generation for stress testing

### 3. Comprehensive Test Coverage
- Edge cases with null/empty inputs
- Parameterized tests with multiple scenarios
- Repeated tests for random data validation
- Custom test data generation

### 4. Mutation Testing Setup
- PITest configuration in Gradle
- Target class specification
- Exclusion of non-business logic classes
- Multiple output formats (HTML, XML)

## Running the Tests

### Execute Unit Tests
```bash
./gradlew test
```

### Run Mutation Tests
```bash
./gradlew pitest
```

### View Mutation Test Reports
After running mutation tests, open:
```
build/reports/pitest/index.html
```

## EasyRandom Examples

### Basic Usage
```java
EasyRandom easyRandom = new EasyRandom();
User randomUser = easyRandom.nextObject(User.class);
```

### Advanced Configuration
```java
EasyRandomParameters parameters = new EasyRandomParameters()
    .seed(123L)
    .objectPoolSize(100)
    .randomizationDepth(3)
    .stringLengthRange(5, 50)
    .collectionSizeRange(1, 10)
    .randomize(FieldPredicates.named("email"), new EmailRandomizer())
    .excludeField(FieldPredicates.named("id"));

EasyRandom customRandom = new EasyRandom(parameters);
```

### Property-Based Testing Example
```java
@RepeatedTest(50)
void propertyTest_UserDiscountShouldAlwaysBeInValidRange() {
    User user = easyRandom.nextObject(User.class);
    user.setActive(true);
    
    double discount = userService.calculateDiscount(user);
    
    assertTrue(discount >= 0.0 && discount <= 0.15);
}
```

## Business Logic Tested

### UserService
- Age validation (adult/minor/senior)
- Email format validation
- User activation eligibility
- Discount calculation based on age categories

### OrderService
- Order total calculation
- Cancellation eligibility
- Shipping cost calculation
- Express delivery eligibility
- Order status transitions
- Recent order detection

## Mutation Testing Benefits

1. **Test Quality Assessment**: Identifies weak spots in your test suite
2. **Higher Confidence**: Ensures tests actually validate business logic
3. **Edge Case Discovery**: Reveals untested scenarios
4. **Refactoring Safety**: Provides confidence when changing code

## Key Insights from This POC

1. **EasyRandom excels at generating diverse test data** that helps uncover edge cases
2. **Property-based testing** validates business invariants across many inputs
3. **Mutation testing reveals** whether tests actually verify business logic or just execute code
4. **Custom randomizers** allow domain-specific test data generation
5. **Repeated tests with random data** increase confidence in business logic robustness

## Running Mutation Tests

The PITest configuration targets:
- All classes in `org.example.easyrandompoc.*`
- Excludes the main application class
- Uses 4 threads for faster execution
- Generates both HTML and XML reports

### Expected Mutation Score
A good mutation score is typically:
- **60-70%**: Acceptable for most projects
- **70-80%**: Good test coverage
- **80%+**: Excellent test coverage

## Best Practices Demonstrated

1. **Comprehensive null checking** in all business methods
2. **Edge case testing** with empty collections and invalid inputs
3. **Parameterized tests** for multiple scenario validation
4. **Custom EasyRandom configurations** for realistic test data
5. **Property-based testing** for business invariant validation
6. **Bulk data generation** for performance and stress testing

## Next Steps

To extend this POC:

1. Add more complex domain models
2. Implement repository layer with database interactions
3. Add integration tests with TestContainers
4. Explore advanced PITest operators
5. Implement custom mutation operators
6. Add performance benchmarking with JMH

## Resources

- [EasyRandom Documentation](https://github.com/j-easy/easy-random)
- [PITest Documentation](https://pitest.org/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Property-Based Testing](https://hypothesis.works/articles/what-is-property-based-testing/)
