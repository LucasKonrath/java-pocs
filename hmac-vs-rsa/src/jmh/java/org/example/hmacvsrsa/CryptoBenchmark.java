package org.example.hmacvsrsa;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class CryptoBenchmark {

    private CryptoUtils.HmacOperations hmacSha256;
    private CryptoUtils.HmacOperations hmacSha512;
    private CryptoUtils.RsaOperations rsa2048;
    private CryptoUtils.RsaOperations rsa4096;

    private byte[] smallData;
    private byte[] mediumData;
    private byte[] largeData;

    private byte[] hmacSha256Signature;
    private byte[] hmacSha512Signature;
    private byte[] rsaSignature2048;
    private byte[] rsaSignature4096;

    private byte[] encryptedDataRsa2048;
    private byte[] encryptedDataRsa4096;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        // Initialize HMAC operations
        byte[] hmacKey256 = CryptoUtils.generateHmacKey(32); // 256 bits
        byte[] hmacKey512 = CryptoUtils.generateHmacKey(64); // 512 bits
        hmacSha256 = new CryptoUtils.HmacOperations("HmacSHA256", hmacKey256);
        hmacSha512 = new CryptoUtils.HmacOperations("HmacSHA512", hmacKey512);

        // Initialize RSA operations
        rsa2048 = new CryptoUtils.RsaOperations(2048);
        rsa4096 = new CryptoUtils.RsaOperations(4096);

        // Generate test data of different sizes
        smallData = CryptoUtils.generateRandomData(64);     // 64 bytes
        mediumData = CryptoUtils.generateRandomData(1024);  // 1KB
        largeData = CryptoUtils.generateRandomData(10240);  // 10KB

        // Pre-generate signatures for verification benchmarks
        hmacSha256Signature = hmacSha256.sign(smallData);
        hmacSha512Signature = hmacSha512.sign(smallData);
        rsaSignature2048 = rsa2048.sign(smallData);
        rsaSignature4096 = rsa4096.sign(smallData);

        // Pre-encrypt data for decryption benchmarks (using small data due to RSA limitations)
        encryptedDataRsa2048 = rsa2048.encrypt(smallData);
        encryptedDataRsa4096 = rsa4096.encrypt(smallData);
    }

    // HMAC Signing Benchmarks
    @Benchmark
    public byte[] hmacSha256SignSmall() {
        return hmacSha256.sign(smallData);
    }

    @Benchmark
    public byte[] hmacSha256SignMedium() {
        return hmacSha256.sign(mediumData);
    }

    @Benchmark
    public byte[] hmacSha256SignLarge() {
        return hmacSha256.sign(largeData);
    }

    @Benchmark
    public byte[] hmacSha512SignSmall() {
        return hmacSha512.sign(smallData);
    }

    @Benchmark
    public byte[] hmacSha512SignMedium() {
        return hmacSha512.sign(mediumData);
    }

    @Benchmark
    public byte[] hmacSha512SignLarge() {
        return hmacSha512.sign(largeData);
    }

    // HMAC Verification Benchmarks
    @Benchmark
    public boolean hmacSha256Verify() {
        return hmacSha256.verify(smallData, hmacSha256Signature);
    }

    @Benchmark
    public boolean hmacSha512Verify() {
        return hmacSha512.verify(smallData, hmacSha512Signature);
    }

    // RSA Signing Benchmarks
    @Benchmark
    public byte[] rsaSign2048() throws Exception {
        return rsa2048.sign(smallData);
    }

    @Benchmark
    public byte[] rsaSign4096() throws Exception {
        return rsa4096.sign(smallData);
    }

    // RSA Verification Benchmarks
    @Benchmark
    public boolean rsaVerify2048() throws Exception {
        return rsa2048.verify(smallData, rsaSignature2048);
    }

    @Benchmark
    public boolean rsaVerify4096() throws Exception {
        return rsa4096.verify(smallData, rsaSignature4096);
    }

    // RSA Encryption Benchmarks
    @Benchmark
    public byte[] rsaEncrypt2048() throws Exception {
        return rsa2048.encrypt(smallData);
    }

    @Benchmark
    public byte[] rsaEncrypt4096() throws Exception {
        return rsa4096.encrypt(smallData);
    }

    // RSA Decryption Benchmarks
    @Benchmark
    public byte[] rsaDecrypt2048() throws Exception {
        return rsa2048.decrypt(encryptedDataRsa2048);
    }

    @Benchmark
    public byte[] rsaDecrypt4096() throws Exception {
        return rsa4096.decrypt(encryptedDataRsa4096);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CryptoBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
