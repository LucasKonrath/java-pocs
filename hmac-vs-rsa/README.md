# HMAC vs RSA Performance Comparison POC

This project demonstrates and benchmarks the performance differences between HMAC (Hash-based Message Authentication Code) and RSA cryptographic operations using JMH (Java Microbenchmark Harness).

## Overview

The POC compares:
- **HMAC SHA256/SHA512**: Symmetric key-based message authentication
- **RSA 2048/4096**: Asymmetric key-based encryption, decryption, signing, and verification

## Project Structure

- `CryptoUtils.java`: Utility classes for HMAC and RSA operations
- `CryptoBenchmark.java`: JMH benchmark class with comprehensive performance tests
- `HmacVsRsaApplication.java`: Spring Boot application with demo and benchmark runner

## Running the POC

### 1. Quick Demo
```bash
./gradlew bootRun
```
This runs a basic demonstration showing both HMAC and RSA operations with sample data.

### 2. Full JMH Benchmarks
```bash
./gradlew jmh
```
This runs comprehensive JMH benchmarks comparing all operations.

### 3. Benchmarks via Spring Boot
```bash
./gradlew bootRun --args='benchmark'
```
Alternative way to run benchmarks through the Spring Boot application.

## Benchmark Tests

The benchmark includes the following test scenarios:

### HMAC Operations
- **Signing**: Small (64B), Medium (1KB), Large (10KB) data with SHA256 and SHA512
- **Verification**: Message verification with pre-generated signatures

### RSA Operations
- **Signing**: Digital signatures with 2048-bit and 4096-bit keys
- **Verification**: Signature verification with different key sizes
- **Encryption**: Data encryption (limited to small data due to RSA constraints)
- **Decryption**: Data decryption with different key sizes

## Expected Results

Based on cryptographic theory, you should observe:

1. **HMAC**: Extremely fast for all operations, scales well with data size
2. **RSA Signing**: Much slower than HMAC, 4096-bit significantly slower than 2048-bit
3. **RSA Verification**: Faster than RSA signing but still slower than HMAC
4. **RSA Encryption/Decryption**: Slowest operations, limited by key size constraints

## Technical Details

- **Java Version**: 24
- **JMH Version**: 1.37
- **Spring Boot Version**: 3.5.5
- **Benchmark Mode**: Average time measurement in microseconds
- **Key Sizes**: HMAC (256/512-bit), RSA (2048/4096-bit)

## Use Cases

This POC helps understand when to use each cryptographic approach:

- **HMAC**: High-throughput scenarios, symmetric key environments, message authentication
- **RSA**: Asymmetric key scenarios, digital signatures, small data encryption, key exchange

## Building and Testing

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Clean and rebuild
./gradlew clean build
```
