# HMAC vs RSA Performance Benchmark Results Analysis

## Summary of Results

The JMH benchmark successfully ran and provided the following performance measurements (average time per operation in microseconds):

## HMAC Performance (SHA256)
- **Small data (64B)**: 0.097 μs/op
- **Medium data (1KB)**: 0.391 μs/op  
- **Large data (10KB)**: 3.205 μs/op
- **Verification**: 0.113 μs/op

## HMAC Performance (SHA512)
- **Small data (64B)**: 0.281 μs/op
- **Medium data (1KB)**: 0.831 μs/op
- **Large data (10KB)**: 5.808 μs/op
- **Verification**: 0.312 μs/op

## RSA Performance (2048-bit)
- **Signing**: 549.512 μs/op
- **Verification**: 21.130 μs/op
- **Encryption**: 21.757 μs/op
- **Decryption**: 520.746 μs/op

## RSA Performance (4096-bit)
- **Signing**: 3,466.195 μs/op
- **Verification**: 69.553 μs/op
- **Encryption**: 70.621 μs/op
- **Decryption**: 3,354.386 μs/op

## Key Performance Insights

### Speed Comparison (Operations per Second)
- **HMAC SHA256 (small)**: ~10,309,278 ops/sec
- **RSA 2048 signing**: ~1,820 ops/sec
- **RSA 4096 signing**: ~288 ops/sec

### Performance Ratios
1. **HMAC vs RSA Signing (2048-bit)**: HMAC is ~5,665x faster
2. **HMAC vs RSA Signing (4096-bit)**: HMAC is ~35,733x faster
3. **RSA Verification vs Signing**: RSA verification is ~26x faster than signing (2048-bit)
4. **RSA 2048 vs 4096**: 4096-bit operations are ~6.3x slower than 2048-bit

### Data Size Impact on HMAC
- **SHA256**: Performance scales linearly with data size
- **SHA512**: Roughly 2-3x slower than SHA256 but still extremely fast

### RSA Key Size Impact
- **4096-bit vs 2048-bit**: 
  - Signing: ~6.3x slower
  - Verification: ~3.3x slower
  - Encryption: ~3.2x slower
  - Decryption: ~6.4x slower

## Practical Implications

### When to Use HMAC
- High-throughput scenarios (millions of operations per second)
- Real-time applications requiring minimal latency
- Symmetric key environments
- Message authentication where both parties share a secret

### When to Use RSA
- Asymmetric key scenarios (no shared secret)
- Digital signatures for non-repudiation
- Small data encryption or key exchange
- When verification needs to be done by multiple parties

### Security vs Performance Trade-offs
- **HMAC**: Extremely fast but requires shared secret key
- **RSA 2048**: Good security-performance balance for most applications
- **RSA 4096**: Higher security but significant performance cost

## Environment Details
- **JVM**: Java HotSpot 64-Bit Server VM, Java 24
- **JMH Version**: 1.36
- **Benchmark Mode**: Average time per operation
- **Iterations**: 3 measurement iterations with 2 warmup iterations
