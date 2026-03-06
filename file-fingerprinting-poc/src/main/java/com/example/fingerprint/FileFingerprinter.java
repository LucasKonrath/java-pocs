package com.example.fingerprint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileFingerprinter {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a file path as an argument.");
            return;
        }

        String filePath = args[0];
        try {
            String sha256 = calculateSHA256(filePath);
            System.out.println("SHA-256 Fingerprint: " + sha256);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error calculating fingerprint: " + e.getMessage());
        }
    }

    private static String calculateSHA256(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = new FileInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            // Read the file to update the message digest
            while (dis.read() != -1) ;
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}