# Comprehensive Cryptographic Performance Comparison POC

This project demonstrates and benchmarks the performance differences between various cryptographic algorithms using JMH (Java Microbenchmark Harness).

## Overview

The POC compares multiple categories of cryptographic operations:

### Digital Signatures & Message Authentication
- **HMAC SHA256/SHA512**: Symmetric key-based message authentication
- **RSA 2048/4096**: Asymmetric digital signatures
- **ECDSA P-256/P-384/P-521**: Elliptic curve digital signatures

### Encryption & Decryption
- **RSA 2048/4096**: Asymmetric encryption (small data only)
- **AES-128/256 CBC**: Symmetric block cipher encryption
- **AES-128/256 GCM**: Authenticated symmetric encryption

## Project Structure

- `CryptoUtils.java`: Utility classes for all cryptographic operations
- `CryptoBenchmark.java`: Comprehensive JMH benchmark suite
- `HmacVsRsaApplication.java`: Spring Boot application with demo

## Running the POC

### 1. Quick Demo
```bash
./gradlew bootRun
```
Shows basic operations for all supported algorithms.

### 2. Full JMH Benchmarks
```bash
./gradlew jmh
```
Runs comprehensive performance benchmarks across all algorithms.

## Benchmark Categories

### Message Authentication & Digital Signatures
- **HMAC**: SHA256/SHA512 with different data sizes (64B, 1KB, 10KB)
- **RSA**: 2048/4096-bit signing and verification
- **ECDSA**: P-256, P-384, P-521 curve signing and verification

### Symmetric Encryption
- **AES-CBC**: 128/256-bit encryption/decryption with multiple data sizes
- **AES-GCM**: 128/256-bit authenticated encryption/decryption

### Asymmetric Encryption
- **RSA**: 2048/4096-bit encryption/decryption (small data only)

## Expected Performance Hierarchy

1. **Fastest**: HMAC operations (>10M ops/sec)
2. **Very Fast**: AES encryption/decryption (~1M ops/sec)
3. **Fast**: ECDSA verification, RSA verification
4. **Moderate**: ECDSA signing
5. **Slow**: RSA signing and encryption
6. **Slowest**: RSA decryption (private key operations)

## Algorithm Characteristics

### When to Use Each Algorithm

- **HMAC**: High-throughput message authentication, API signatures
- **AES**: Bulk data encryption, file encryption, database encryption
- **ECDSA**: Modern digital signatures, TLS certificates, blockchain
- **RSA**: Legacy compatibility, key exchange, small data encryption

### Security vs Performance Trade-offs
- **ECDSA P-256**: Best balance of security and performance
- **RSA 2048**: Adequate security, moderate performance
- **RSA 4096**: High security, significant performance cost
- **AES-256**: Maximum symmetric security, minimal performance impact
