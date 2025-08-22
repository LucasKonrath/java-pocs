# Comprehensive Cryptographic Performance Benchmark Results Analysis

## Enhanced POC Overview

This benchmark now includes **5 major cryptographic algorithm families**:
- **HMAC** (SHA256/SHA512): Message authentication
- **RSA** (2048/4096-bit): Asymmetric signatures and encryption  
- **ECDSA** (P-256/P-384/P-521): Elliptic curve digital signatures
- **AES-CBC** (128/256-bit): Symmetric block cipher encryption
- **AES-GCM** (128/256-bit): Authenticated symmetric encryption

## Complete Benchmark Results (Measured Performance)

### HMAC Performance (SHA256)
- **Small data (64B)**: 0.097 μs/op (~10.3M ops/sec)
- **Medium data (1KB)**: 0.391 μs/op (~2.6M ops/sec)  
- **Large data (10KB)**: 3.169 μs/op (~315K ops/sec)
- **Verification**: 0.113 μs/op (~8.8M ops/sec)

### HMAC Performance (SHA512)
- **Small data (64B)**: 0.281 μs/op (~3.6M ops/sec)
- **Medium data (1KB)**: 0.831 μs/op (~1.2M ops/sec)
- **Large data (10KB)**: 5.808 μs/op (~172K ops/sec)
- **Verification**: 0.312 μs/op (~3.2M ops/sec)

### RSA Performance (2048-bit)
- **Signing**: 549.512 μs/op (~1,820 ops/sec)
- **Verification**: 21.130 μs/op (~47,327 ops/sec)
- **Encryption**: 21.757 μs/op (~45,966 ops/sec)
- **Decryption**: 520.746 μs/op (~1,920 ops/sec)

### RSA Performance (4096-bit)
- **Signing**: 3,466.195 μs/op (~288 ops/sec)
- **Verification**: 69.553 μs/op (~14,378 ops/sec)
- **Encryption**: 70.621 μs/op (~14,160 ops/sec)
- **Decryption**: 3,354.386 μs/op (~298 ops/sec)

### ECDSA Performance (Measured Results)
- **P-256 Signing**: 66.302 μs/op (~15,082 ops/sec)
- **P-256 Verification**: 216.485 μs/op (~4,619 ops/sec)
- **P-384 Signing**: 378.879 μs/op (~2,639 ops/sec)
- **P-384 Verification**: 703.007 μs/op (~1,422 ops/sec)
- **P-521 Signing**: 723.407 μs/op (~1,382 ops/sec)
- **P-521 Verification**: 1,301.618 μs/op (~768 ops/sec)

### AES-CBC Performance (Measured Results)
#### AES-128 CBC
- **Small data (64B)**: 0.038 μs/op (~26.3M ops/sec)
- **Medium data (1KB)**: 0.438 μs/op (~2.3M ops/sec)
- **Large data (10KB)**: 4.406 μs/op (~227K ops/sec)
- **Decryption Small**: 0.034 μs/op (~29.4M ops/sec)
- **Decryption Medium**: 0.122 μs/op (~8.2M ops/sec)
- **Decryption Large**: 1.229 μs/op (~814K ops/sec)

#### AES-256 CBC
- **Small data (64B)**: 0.051 μs/op (~19.6M ops/sec)
- **Medium data (1KB)**: 0.569 μs/op (~1.8M ops/sec)
- **Large data (10KB)**: 5.501 μs/op (~182K ops/sec)
- **Decryption Small**: 0.043 μs/op (~23.3M ops/sec)
- **Decryption Medium**: 0.136 μs/op (~7.4M ops/sec)
- **Decryption Large**: 1.331 μs/op (~751K ops/sec)

### AES-GCM Performance (Measured Results)
#### AES-128 GCM
- **Small data (64B)**: 0.217 μs/op (~4.6M ops/sec)
- **Medium data (1KB)**: 0.408 μs/op (~2.5M ops/sec)
- **Large data (10KB)**: 2.504 μs/op (~399K ops/sec)
- **Decryption Small**: 0.076 μs/op (~13.2M ops/sec)
- **Decryption Medium**: 0.260 μs/op (~3.8M ops/sec)
- **Decryption Large**: 2.447 μs/op (~409K ops/sec)

#### AES-256 GCM
- **Small data (64B)**: 0.235 μs/op (~4.3M ops/sec)
- **Medium data (1KB)**: 0.443 μs/op (~2.3M ops/sec)
- **Large data (10KB)**: 2.649 μs/op (~378K ops/sec)
- **Decryption Small**: 0.085 μs/op (~11.8M ops/sec)
- **Decryption Medium**: 0.284 μs/op (~3.5M ops/sec)
- **Decryption Large**: 2.626 μs/op (~381K ops/sec)

## Performance Ranking (Operations per Second)

### Ultra-Fast (>10M ops/sec)
1. **AES-128 Decrypt Small**: ~29.4M ops/sec
2. **AES-128 Encrypt Small**: ~26.3M ops/sec
3. **AES-256 Decrypt Small**: ~23.3M ops/sec
4. **AES-256 Encrypt Small**: ~19.6M ops/sec
5. **AES-128 GCM Decrypt Small**: ~13.2M ops/sec
6. **AES-256 GCM Decrypt Small**: ~11.8M ops/sec
7. **HMAC SHA256 Small**: ~10.3M ops/sec

### Very Fast (1M-10M ops/sec)
8. **HMAC SHA256 Verify**: ~8.8M ops/sec
9. **AES-128 Decrypt Medium**: ~8.2M ops/sec
10. **AES-256 Decrypt Medium**: ~7.4M ops/sec
11. **AES-128 GCM Decrypt Medium**: ~3.8M ops/sec
12. **AES-256 GCM Decrypt Medium**: ~3.5M ops/sec
13. **HMAC SHA512 Small**: ~3.6M ops/sec
14. **HMAC SHA256 Medium**: ~2.6M ops/sec
15. **AES-128 GCM Encrypt Medium**: ~2.5M ops/sec
16. **AES-128 Encrypt Medium**: ~2.3M ops/sec

### Fast (100K-1M ops/sec)
17. **AES Encrypt/Decrypt Large**: ~182K-814K ops/sec
18. **AES-GCM Large**: ~378K-409K ops/sec
19. **HMAC Large**: ~172K-315K ops/sec

### Moderate (1K-100K ops/sec)
20. **RSA 2048 Verification**: ~47K ops/sec
21. **ECDSA P-256 Signing**: ~15K ops/sec
22. **RSA 4096 Verification**: ~14K ops/sec
23. **ECDSA P-256 Verification**: ~4.6K ops/sec
24. **ECDSA P-384 Signing**: ~2.6K ops/sec
25. **RSA 2048 Signing**: ~1.8K ops/sec
26. **ECDSA P-384 Verification**: ~1.4K ops/sec
27. **ECDSA P-521 Signing**: ~1.4K ops/sec

### Slow (<1K ops/sec)
28. **ECDSA P-521 Verification**: ~768 ops/sec
29. **RSA 4096 Decryption**: ~298 ops/sec
30. **RSA 4096 Signing**: ~288 ops/sec

## Key Performance Insights

### Algorithm Family Speed Comparison
- **AES Encryption**: 182K to 29.4M ops/sec (fastest overall)
- **HMAC**: 172K to 10.3M ops/sec (very fast authentication)
- **RSA Operations**: 288 to 47K ops/sec (moderate to slow)
- **ECDSA Operations**: 768 to 15K ops/sec (moderate digital signatures)

### Crypto Operation Type Performance
- **Symmetric Encryption (AES)**: Fastest for all data sizes
- **Message Authentication (HMAC)**: Very fast, excellent for APIs
- **Digital Signature Verification**: Faster than signing for all algorithms
- **Digital Signature Creation**: Slowest operations overall

### Data Size Impact
- **Small Data (64B)**: AES dominates, HMAC very competitive
- **Medium Data (1KB)**: AES maintains advantage, gap narrows
- **Large Data (10KB)**: Performance converges, AES still leads

### Key Size Impact Analysis
- **AES**: 256-bit only ~25% slower than 128-bit
- **RSA**: 4096-bit ~6x slower than 2048-bit
- **ECDSA**: P-521 ~10x slower than P-256

### Interesting Findings
- **ECDSA Verification Anomaly**: Verification is slower than signing (unusual)
- **AES-GCM vs CBC**: GCM adds ~85% overhead for authentication
- **HMAC vs AES**: HMAC competitive with AES for small data
- **RSA vs ECDSA**: ECDSA P-256 signing ~8x faster than RSA 2048

## Security vs Performance Recommendations

### High-Throughput Scenarios (>1M ops/sec needed)
- **Bulk Encryption**: AES-128 CBC or AES-256 GCM
- **Message Authentication**: HMAC SHA256
- **Not suitable**: Any digital signature algorithm

### Balanced Security/Performance
- **Digital Signatures**: ECDSA P-256 (15K ops/sec)
- **Authenticated Encryption**: AES-256 GCM
- **Message Auth**: HMAC SHA256

### Maximum Security (Performance Secondary)
- **Digital Signatures**: ECDSA P-521 or RSA 4096
- **Encryption**: AES-256 GCM
- **Message Auth**: HMAC SHA512

## Technical Environment
- **JVM**: Java HotSpot 64-Bit Server VM, Java 24
- **JMH Version**: 1.36
- **Benchmark Mode**: Average time per operation
- **Test Matrix**: 46 individual benchmark tests across 5 algorithm families
