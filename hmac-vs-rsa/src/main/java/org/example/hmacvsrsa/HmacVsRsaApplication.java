package org.example.hmacvsrsa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HmacVsRsaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HmacVsRsaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("HMAC vs RSA Performance Comparison POC");
        System.out.println("=======================================");

        if (args.length > 0 && "benchmark".equals(args[0])) {
            System.out.println("Use './gradlew jmh' to run JMH benchmarks directly.");
            System.out.println("The benchmarks are now in the dedicated JMH source directory.");
        } else {
            System.out.println("To run benchmarks, use: ./gradlew jmh");
            System.out.println("For a quick demo, run without arguments (current mode)");

            // Quick demonstration of the crypto operations
            demonstrateCryptoOperations();
        }
    }

    private void demonstrateCryptoOperations() throws Exception {
        System.out.println("\nDemonstrating crypto operations:");

        byte[] data = "Hello, World!".getBytes();

        // HMAC demonstration
        byte[] hmacKey = CryptoUtils.generateHmacKey(32);
        CryptoUtils.HmacOperations hmac = new CryptoUtils.HmacOperations("HmacSHA256", hmacKey);
        byte[] hmacSignature = hmac.sign(data);
        boolean hmacValid = hmac.verify(data, hmacSignature);

        System.out.println("HMAC SHA256:");
        System.out.println("  Data: " + new String(data));
        System.out.println("  Signature length: " + hmacSignature.length + " bytes");
        System.out.println("  Verification: " + hmacValid);

        // RSA demonstration
        CryptoUtils.RsaOperations rsa = new CryptoUtils.RsaOperations(2048);
        byte[] rsaSignature = rsa.sign(data);
        boolean rsaValid = rsa.verify(data, rsaSignature);
        byte[] rsaEncrypted = rsa.encrypt(data);
        byte[] rsaDecrypted = rsa.decrypt(rsaEncrypted);

        System.out.println("\nRSA 2048:");
        System.out.println("  Data: " + new String(data));
        System.out.println("  Signature length: " + rsaSignature.length + " bytes");
        System.out.println("  Verification: " + rsaValid);
        System.out.println("  Encrypted length: " + rsaEncrypted.length + " bytes");
        System.out.println("  Decrypted: " + new String(rsaDecrypted));

        // ECDSA demonstration
        CryptoUtils.EcdsaOperations ecdsa = new CryptoUtils.EcdsaOperations("secp256r1");
        byte[] ecdsaSignature = ecdsa.sign(data);
        boolean ecdsaValid = ecdsa.verify(data, ecdsaSignature);

        System.out.println("\nECDSA P-256:");
        System.out.println("  Data: " + new String(data));
        System.out.println("  Signature length: " + ecdsaSignature.length + " bytes");
        System.out.println("  Verification: " + ecdsaValid);

        // AES demonstration
        CryptoUtils.AesOperations aes = new CryptoUtils.AesOperations(256);
        byte[] aesEncrypted = aes.encrypt(data);
        byte[] aesDecrypted = aes.decrypt(aesEncrypted);

        System.out.println("\nAES-256 CBC:");
        System.out.println("  Data: " + new String(data));
        System.out.println("  Encrypted length: " + aesEncrypted.length + " bytes");
        System.out.println("  Decrypted: " + new String(aesDecrypted));

        // AES-GCM demonstration
        CryptoUtils.AesOperations.AesGcmOperations aesGcm = new CryptoUtils.AesOperations.AesGcmOperations(256);
        byte[] aesGcmEncrypted = aesGcm.encrypt(data);
        byte[] aesGcmDecrypted = aesGcm.decrypt(aesGcmEncrypted);

        System.out.println("\nAES-256 GCM:");
        System.out.println("  Data: " + new String(data));
        System.out.println("  Encrypted length: " + aesGcmEncrypted.length + " bytes");
        System.out.println("  Decrypted: " + new String(aesGcmDecrypted));

        System.out.println("\nExpected performance characteristics:");
        System.out.println("- HMAC: Very fast for both signing and verification");
        System.out.println("- RSA Signing: Slower, especially with larger key sizes");
        System.out.println("- RSA Verification: Faster than signing but slower than HMAC");
        System.out.println("- RSA Encryption/Decryption: Limited by key size, slow for large data");
        System.out.println("- ECDSA: Faster than RSA, comparable security with smaller keys");
        System.out.println("- AES: Very fast symmetric encryption, scales well with data size");
        System.out.println("- AES-GCM: Slightly slower than CBC but provides authentication");
    }
}
