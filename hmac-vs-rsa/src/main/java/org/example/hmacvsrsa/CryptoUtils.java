package org.example.hmacvsrsa;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.Cipher;

public class CryptoUtils {

    public static class HmacOperations {
        private final Mac mac;

        public HmacOperations(String algorithm, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
            this.mac = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
            this.mac.init(secretKey);
        }

        public byte[] sign(byte[] data) {
            return mac.doFinal(data);
        }

        public boolean verify(byte[] data, byte[] signature) {
            byte[] computed = mac.doFinal(data);
            return MessageDigest.isEqual(computed, signature);
        }
    }

    public static class RsaOperations {
        private final PrivateKey privateKey;
        private final PublicKey publicKey;
        private final Cipher encryptCipher;
        private final Cipher decryptCipher;

        public RsaOperations(int keySize) throws Exception {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();

            this.encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            this.decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }

        public byte[] encrypt(byte[] data) throws Exception {
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return encryptCipher.doFinal(data);
        }

        public byte[] decrypt(byte[] encryptedData) throws Exception {
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return decryptCipher.doFinal(encryptedData);
        }

        public byte[] sign(byte[] data) throws Exception {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        }

        public boolean verify(byte[] data, byte[] signatureBytes) throws Exception {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        }
    }

    public static class EcdsaOperations {
        private final PrivateKey privateKey;
        private final PublicKey publicKey;

        public EcdsaOperations(String curve) throws Exception {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(curve);
            keyPairGenerator.initialize(ecSpec);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        }

        public byte[] sign(byte[] data) throws Exception {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        }

        public boolean verify(byte[] data, byte[] signatureBytes) throws Exception {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }
    }

    public static class AesOperations {
        private final SecretKey secretKey;
        private final Cipher cipher;
        private final byte[] iv;

        public AesOperations(int keySize) throws Exception {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keySize);
            this.secretKey = keyGenerator.generateKey();

            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Generate a fixed IV for consistent benchmarking
            this.iv = new byte[16];
            new SecureRandom().nextBytes(iv);
        }

        public byte[] encrypt(byte[] data) throws Exception {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(data);
        }

        public byte[] decrypt(byte[] encryptedData) throws Exception {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return cipher.doFinal(encryptedData);
        }

        // AES-GCM variant for authenticated encryption
        public static class AesGcmOperations {
            private final SecretKey secretKey;
            private final Cipher cipher;

            public AesGcmOperations(int keySize) throws Exception {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(keySize);
                this.secretKey = keyGenerator.generateKey();

                this.cipher = Cipher.getInstance("AES/GCM/NoPadding");
            }

            public byte[] encrypt(byte[] data) throws Exception {
                // Generate a new IV for each encryption operation (required for GCM security)
                byte[] iv = new byte[12];
                new SecureRandom().nextBytes(iv);

                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
                byte[] encrypted = cipher.doFinal(data);

                // Prepend IV to encrypted data for decryption
                byte[] result = new byte[iv.length + encrypted.length];
                System.arraycopy(iv, 0, result, 0, iv.length);
                System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
                return result;
            }

            public byte[] decrypt(byte[] encryptedDataWithIv) throws Exception {
                // Extract IV from the beginning of the encrypted data
                byte[] iv = new byte[12];
                System.arraycopy(encryptedDataWithIv, 0, iv, 0, 12);

                // Extract encrypted data
                byte[] encryptedData = new byte[encryptedDataWithIv.length - 12];
                System.arraycopy(encryptedDataWithIv, 12, encryptedData, 0, encryptedData.length);

                cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
                return cipher.doFinal(encryptedData);
            }
        }
    }

    public static byte[] generateRandomData(int size) {
        byte[] data = new byte[size];
        new SecureRandom().nextBytes(data);
        return data;
    }

    public static byte[] generateHmacKey(int size) {
        byte[] key = new byte[size];
        new SecureRandom().nextBytes(key);
        return key;
    }
}
