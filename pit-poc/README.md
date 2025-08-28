# Mutation Testing POC with PIT (Pitest)

This project demonstrates mutation testing using PIT (Pitest) in a Spring Boot Java application. Mutation testing is a technique used to evaluate the quality of software tests by introducing small changes (mutations) to the source code and checking if the tests can detect these changes.

## What is Mutation Testing?

Mutation testing works by:
1. Creating mutants (modified versions) of your source code
2. Running your test suite against each mutant
3. Checking if tests fail when they encounter the mutated code
4. Calculating a mutation score based on how many mutants were "killed" by failing tests

A high mutation score indicates that your tests are effective at detecting code changes, which suggests good test quality.

## Project Structure

```
src/
├── main/java/org/example/pitpoc/
│   ├── PitPocApplication.java      # Spring Boot main application
│   ├── Calculator.java             # Simple calculator with basic operations
│   └── BankingService.java         # Complex service with business logic
└── test/java/org/example/pitpoc/
    ├── PitPocApplicationTests.java # Basic Spring Boot test
    ├── CalculatorTest.java         # Comprehensive calculator tests
    └── BankingServiceTest.java     # Comprehensive banking service tests
```

## Classes Under Test

### Calculator.java
A simple calculator class demonstrating basic mutations:
- Arithmetic operations (add, subtract, multiply, divide)
- Conditional logic (isEven, max)
- Recursive methods (factorial)
- Complex conditionals (getGrade)

### BankingService.java
A more complex service demonstrating advanced mutations:
- String validation and regex matching
- Mathematical calculations with compound conditions
- Switch statements
- List operations
- Multiple conditional branches

## Running Mutation Tests

### Prerequisites
- Java 24
- Gradle

### Commands

1. **Run regular tests first:**
   ```bash
   ./gradlew test
   ```

2. **Run mutation tests:**
   ```bash
   ./gradlew pitest
   ```

3. **View results:**
   After running mutation tests, open the HTML report:
   ```
   build/reports/pitest/index.html
   ```

## PIT Configuration

The PIT plugin is configured in `build.gradle` with the following settings:

```groovy
pitest {
    targetClasses = ['org.example.pitpoc.*']  // Classes to mutate
    threads = 4                               // Parallel execution
    outputFormats = ['XML', 'HTML']          // Report formats
    timestampedReports = false               // Clean report names
    mutators = ['DEFAULTS']                  // Standard mutators
    junit5PluginVersion = '1.2.1'           // JUnit 5 support
}
```

## Types of Mutations PIT Will Test

PIT applies various mutations to test the robustness of your tests:

### Conditional Boundary Mutations
- Changes `>` to `>=`, `<` to `<=`, etc.
- Example: `if (balance >= 1000)` becomes `if (balance > 1000)`

### Arithmetic Operator Mutations
- Changes `+` to `-`, `*` to `/`, etc.
- Example: `a + b` becomes `a - b`

### Return Value Mutations
- Changes return values
- Example: `return true` becomes `return false`

### Conditional Mutations
- Negates conditions
- Example: `if (condition)` becomes `if (!condition)`

### Method Call Mutations
- Removes method calls or changes behavior
- Example: Method calls might be removed entirely

## Expected Results

With comprehensive tests, you should see:
- **High mutation score (>80%)**: Indicates good test coverage and quality
- **Detailed HTML report**: Shows exactly which mutations were killed/survived
- **Line-by-line analysis**: Identifies weak spots in your test suite

## Understanding the Report

The PIT report will show:
- **Line Coverage**: Percentage of lines executed by tests
- **Mutation Coverage**: Percentage of mutations killed by tests
- **Test Strength**: Overall quality indicator

### Colors in the Report:
- **Green**: All mutations killed (good test coverage)
- **Red**: Some mutations survived (potential test gaps)
- **Pink**: No mutations generated (may need more complex logic)

## Common Mutation Survivors

Mutations that commonly survive indicate test weaknesses:

1. **Boundary conditions**: Tests might not check edge cases
2. **Return value changes**: Tests might not assert return values
3. **Conditional negation**: Tests might not cover all branches
4. **Arithmetic changes**: Tests might not validate calculations precisely

## Improving Test Quality

To increase mutation score:

1. **Add boundary tests**: Test edge cases and limits
2. **Test all branches**: Ensure every if/else path is tested
3. **Validate return values**: Assert specific expected results
4. **Test error conditions**: Verify exception handling
5. **Add negative tests**: Test what should NOT happen

## Example Mutations You Might See

### In Calculator.java:
- `a + b` → `a - b` (should be caught by addition tests)
- `number % 2 == 0` → `number % 2 != 0` (should be caught by isEven tests)
- `if (a > b)` → `if (a >= b)` (should be caught by max tests)

### In BankingService.java:
- `balance >= 1000` → `balance > 1000` (should be caught by boundary tests)
- `return "PREMIUM"` → `return "BASIC"` (should be caught by assertion tests)
- `services.add(...)` → removed (should be caught by list content tests)

## Best Practices for Mutation Testing

1. **Start with high line coverage**: Ensure basic test coverage first
2. **Focus on critical business logic**: Prioritize important code paths
3. **Use mutation testing to guide test improvements**: Let survivors show you test gaps
4. **Don't aim for 100%**: Some mutations may be equivalent or irrelevant
5. **Regular execution**: Include in CI/CD pipeline for continuous quality monitoring

## Troubleshooting

If mutation tests fail to run:
1. Ensure all regular tests pass first
2. Check Java version compatibility
3. Verify PIT plugin version supports your Java version
4. Review target class patterns in configuration

This POC demonstrates how mutation testing can reveal the true effectiveness of your test suite beyond simple line coverage metrics.
