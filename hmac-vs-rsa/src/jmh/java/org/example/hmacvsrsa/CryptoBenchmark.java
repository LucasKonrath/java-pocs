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

    // New ECDSA operations
    private CryptoUtils.EcdsaOperations ecdsaP256;
    private CryptoUtils.EcdsaOperations ecdsaP384;
    private CryptoUtils.EcdsaOperations ecdsaP521;

    // New AES operations
    private CryptoUtils.AesOperations aes128;
    private CryptoUtils.AesOperations aes256;
    private CryptoUtils.AesOperations.AesGcmOperations aesGcm128;
    private CryptoUtils.AesOperations.AesGcmOperations aesGcm256;

    private byte[] smallData;
    private byte[] mediumData;
    private byte[] largeData;

    private byte[] hmacSha256Signature;
    private byte[] hmacSha512Signature;
    private byte[] rsaSignature2048;
    private byte[] rsaSignature4096;

    // New ECDSA signatures
    private byte[] ecdsaP256Signature;
    private byte[] ecdsaP384Signature;
    private byte[] ecdsaP521Signature;

    private byte[] encryptedDataRsa2048;
    private byte[] encryptedDataRsa4096;

    // New AES encrypted data
    private byte[] encryptedAes128Small;
    private byte[] encryptedAes256Small;
    private byte[] encryptedAes128Medium;
    private byte[] encryptedAes256Medium;
    private byte[] encryptedAes128Large;
    private byte[] encryptedAes256Large;
    private byte[] encryptedAesGcm128Small;
    private byte[] encryptedAesGcm256Small;
    private byte[] encryptedAesGcm128Medium;
    private byte[] encryptedAesGcm256Medium;
    private byte[] encryptedAesGcm128Large;
    private byte[] encryptedAesGcm256Large;

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

        // Initialize ECDSA operations
        ecdsaP256 = new CryptoUtils.EcdsaOperations("secp256r1"); // P-256
        ecdsaP384 = new CryptoUtils.EcdsaOperations("secp384r1"); // P-384
        ecdsaP521 = new CryptoUtils.EcdsaOperations("secp521r1"); // P-521

        // Initialize AES operations
        aes128 = new CryptoUtils.AesOperations(128);
        aes256 = new CryptoUtils.AesOperations(256);
        aesGcm128 = new CryptoUtils.AesOperations.AesGcmOperations(128);
        aesGcm256 = new CryptoUtils.AesOperations.AesGcmOperations(256);

        // Generate test data of different sizes
        smallData = CryptoUtils.generateRandomData(64);     // 64 bytes
        mediumData = CryptoUtils.generateRandomData(1024);  // 1KB
        largeData = CryptoUtils.generateRandomData(10240);  // 10KB

        // Pre-generate signatures for verification benchmarks
        hmacSha256Signature = hmacSha256.sign(smallData);
        hmacSha512Signature = hmacSha512.sign(smallData);
        rsaSignature2048 = rsa2048.sign(smallData);
        rsaSignature4096 = rsa4096.sign(smallData);
        ecdsaP256Signature = ecdsaP256.sign(smallData);
        ecdsaP384Signature = ecdsaP384.sign(smallData);
        ecdsaP521Signature = ecdsaP521.sign(smallData);

        // Pre-encrypt data for decryption benchmarks
        encryptedDataRsa2048 = rsa2048.encrypt(smallData);
        encryptedDataRsa4096 = rsa4096.encrypt(smallData);

        // Pre-encrypt AES data for decryption benchmarks
        encryptedAes128Small = aes128.encrypt(smallData);
        encryptedAes256Small = aes256.encrypt(smallData);
        encryptedAes128Medium = aes128.encrypt(mediumData);
        encryptedAes256Medium = aes256.encrypt(mediumData);
        encryptedAes128Large = aes128.encrypt(largeData);
        encryptedAes256Large = aes256.encrypt(largeData);

        encryptedAesGcm128Small = aesGcm128.encrypt(smallData);
        encryptedAesGcm256Small = aesGcm256.encrypt(smallData);
        encryptedAesGcm128Medium = aesGcm128.encrypt(mediumData);
        encryptedAesGcm256Medium = aesGcm256.encrypt(mediumData);
        encryptedAesGcm128Large = aesGcm128.encrypt(largeData);
        encryptedAesGcm256Large = aesGcm256.encrypt(largeData);
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

    // ECDSA Signing Benchmarks
    @Benchmark
    public byte[] ecdsaP256Sign() throws Exception {
        return ecdsaP256.sign(smallData);
    }

    @Benchmark
    public byte[] ecdsaP384Sign() throws Exception {
        return ecdsaP384.sign(smallData);
    }

    @Benchmark
    public byte[] ecdsaP521Sign() throws Exception {
        return ecdsaP521.sign(smallData);
    }

    // ECDSA Verification Benchmarks
    @Benchmark
    public boolean ecdsaP256Verify() throws Exception {
        return ecdsaP256.verify(smallData, ecdsaP256Signature);
    }

    @Benchmark
    public boolean ecdsaP384Verify() throws Exception {
        return ecdsaP384.verify(smallData, ecdsaP384Signature);
    }

    @Benchmark
    public boolean ecdsaP521Verify() throws Exception {
        return ecdsaP521.verify(smallData, ecdsaP521Signature);
    }

    // AES-CBC Encryption Benchmarks
    @Benchmark
    public byte[] aes128EncryptSmall() throws Exception {
        return aes128.encrypt(smallData);
    }

    @Benchmark
    public byte[] aes128EncryptMedium() throws Exception {
        return aes128.encrypt(mediumData);
    }

    @Benchmark
    public byte[] aes128EncryptLarge() throws Exception {
        return aes128.encrypt(largeData);
    }

    @Benchmark
    public byte[] aes256EncryptSmall() throws Exception {
        return aes256.encrypt(smallData);
    }

    @Benchmark
    public byte[] aes256EncryptMedium() throws Exception {
        return aes256.encrypt(mediumData);
    }

    @Benchmark
    public byte[] aes256EncryptLarge() throws Exception {
        return aes256.encrypt(largeData);
    }

    // AES-CBC Decryption Benchmarks
    @Benchmark
    public byte[] aes128DecryptSmall() throws Exception {
        return aes128.decrypt(encryptedAes128Small);
    }

    @Benchmark
    public byte[] aes128DecryptMedium() throws Exception {
        return aes128.decrypt(encryptedAes128Medium);
    }

    @Benchmark
    public byte[] aes128DecryptLarge() throws Exception {
        return aes128.decrypt(encryptedAes128Large);
    }

    @Benchmark
    public byte[] aes256DecryptSmall() throws Exception {
        return aes256.decrypt(encryptedAes256Small);
    }

    @Benchmark
    public byte[] aes256DecryptMedium() throws Exception {
        return aes256.decrypt(encryptedAes256Medium);
    }

    @Benchmark
    public byte[] aes256DecryptLarge() throws Exception {
        return aes256.decrypt(encryptedAes256Large);
    }

    // AES-GCM Encryption Benchmarks
    @Benchmark
    public byte[] aesGcm128EncryptSmall() throws Exception {
        return aesGcm128.encrypt(smallData);
    }

    @Benchmark
    public byte[] aesGcm128EncryptMedium() throws Exception {
        return aesGcm128.encrypt(mediumData);
    }

    @Benchmark
    public byte[] aesGcm128EncryptLarge() throws Exception {
        return aesGcm128.encrypt(largeData);
    }

    @Benchmark
    public byte[] aesGcm256EncryptSmall() throws Exception {
        return aesGcm256.encrypt(smallData);
    }

    @Benchmark
    public byte[] aesGcm256EncryptMedium() throws Exception {
        return aesGcm256.encrypt(mediumData);
    }

    @Benchmark
    public byte[] aesGcm256EncryptLarge() throws Exception {
        return aesGcm256.encrypt(largeData);
    }

    // AES-GCM Decryption Benchmarks
    @Benchmark
    public byte[] aesGcm128DecryptSmall() throws Exception {
        return aesGcm128.decrypt(encryptedAesGcm128Small);
    }

    @Benchmark
    public byte[] aesGcm128DecryptMedium() throws Exception {
        return aesGcm128.decrypt(encryptedAesGcm128Medium);
    }

    @Benchmark
    public byte[] aesGcm128DecryptLarge() throws Exception {
        return aesGcm128.decrypt(encryptedAesGcm128Large);
    }

    @Benchmark
    public byte[] aesGcm256DecryptSmall() throws Exception {
        return aesGcm256.decrypt(encryptedAesGcm256Small);
    }

    @Benchmark
    public byte[] aesGcm256DecryptMedium() throws Exception {
        return aesGcm256.decrypt(encryptedAesGcm256Medium);
    }

    @Benchmark
    public byte[] aesGcm256DecryptLarge() throws Exception {
        return aesGcm256.decrypt(encryptedAesGcm256Large);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CryptoBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
