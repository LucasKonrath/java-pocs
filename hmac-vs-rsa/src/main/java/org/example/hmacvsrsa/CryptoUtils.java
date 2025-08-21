package org.example.hmacvsrsa;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
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
