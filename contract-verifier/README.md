# Spring Cloud Contract Verifier POC

This project demonstrates how to use Spring Cloud Contract Verifier for contract-driven development between microservices.

## Overview

Spring Cloud Contract Verifier helps you implement Consumer Driven Contract (CDC) testing. It ensures that:
- **Producer side**: Your API implementation matches the contract specifications
- **Consumer side**: Your client code works correctly with the API according to the contracts

## Project Structure

```
src/
├── main/java/
│   └── org/ldamaceno/contract_verifier/
│       ├── ContractVerifierApplication.java
│       ├── controller/
│       │   └── UserController.java          # REST API (Producer)
│       └── model/
│           └── User.java                    # Domain model
└── test/
    ├── java/
    │   └── org/ldamaceno/contract_verifier/
    │       ├── BaseContractTest.java        # Base class for generated tests
    │       ├── ConsumerContractTest.java    # Consumer-side tests
    │       └── ContractVerifierApplicationTests.java
    └── resources/
        └── contracts/                       # Contract definitions
            ├── createUser.groovy
            ├── createUserInvalid.groovy
            ├── getUserById.groovy
            └── getUserNotFound.groovy
```

## How It Works

### 1. Contract Definitions (Groovy DSL)
Contracts are defined in Groovy files that specify:
- HTTP request details (method, URL, headers, body)
- Expected HTTP response (status, headers, body)

Example contract (`getUserById.groovy`):
```groovy
Contract.make {
    description "Should return user by id"
    request {
        method GET()
        url "/api/users/1"
    }
    response {
        status OK()
        body(
            id: 1,
            name: "John Doe",
            email: "john.doe@example.com",
            active: true
        )
    }
}
```

### 2. Producer-Side Testing
- `BaseContractTest.java` sets up the test context
- Spring Cloud Contract Maven plugin generates test classes from contracts
- Generated tests verify that your controller implementation matches the contracts

### 3. Consumer-Side Testing
- `ConsumerContractTest.java` demonstrates how a consumer service would test against stubs
- Uses WireMock stubs generated from the same contracts
- Ensures consumer code works with the expected API behavior

## Running the POC

### Step 1: Generate Contract Tests
```bash
mvn clean compile
```
This generates test classes in `target/generated-test-sources/contracts/`

### Step 2: Run Producer-Side Contract Tests
```bash
mvn test
```
This verifies that your implementation matches the contracts.

### Step 3: Generate and Install Stubs
```bash
mvn clean install
```
This creates WireMock stubs and installs them locally for consumer testing.

### Step 4: Run Consumer Tests
The consumer tests use the generated stubs to verify client behavior.

## Key Benefits Demonstrated

1. **Contract-First Development**: Contracts define the API before implementation
2. **Backward Compatibility**: Changes that break contracts will fail tests
3. **Independent Testing**: Consumer and producer teams can work independently
4. **Documentation**: Contracts serve as living documentation
5. **Stub Generation**: Automatic creation of test doubles for integration testing

## API Endpoints

The UserController provides these endpoints:
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users` - Get all users
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Contract Scenarios Covered

1. **Successful GET request** - Returns user data
2. **404 Not Found** - User doesn't exist
3. **Successful POST request** - Creates new user
4. **400 Bad Request** - Invalid user data

## Next Steps

To extend this POC:
1. Add more complex contract scenarios
2. Implement database integration
3. Add authentication/authorization contracts
4. Create separate consumer and producer projects
5. Set up CI/CD pipeline with contract testing
