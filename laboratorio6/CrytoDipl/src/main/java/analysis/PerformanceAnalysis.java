package analysis;

import java.security.*;
import javax.crypto.*;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.nio.file.*;

public class PerformanceAnalysis {

    private static final SecretKey desKey = generateDESKey();

    public static void main(String[] args) {
        try {
            String[] filePaths = {"1MB.bin", "10MB.bin", "100MB.bin"};
            analyzePerformance(filePaths);
            System.out.println("Análisis completado. Resultados guardados en performance_results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void analyzePerformance(String[] filePaths) throws Exception {
        String[] sizes = {"1 MB", "10 MB", "100 MB"};
        StringBuilder csvContent = new StringBuilder("Tamaño,Encr DES,Desencr DES,Encr RSA 1024,Desencr RSA 1024,Encr RSA 2048,Desencr RSA 2048\n");

        KeyPair keyPair1024 = generateRSAKeyPair(1024);
        KeyPair keyPair2048 = generateRSAKeyPair(2048);

        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

            csvContent.append(sizes[i]).append(",");

            // DES
            long startTime = System.nanoTime();
            byte[] encryptedDES = encryptDES(fileContent);
            long endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append(",");

            startTime = System.nanoTime();
            decryptDES(encryptedDES);
            endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append(",");

            // RSA 1024
            startTime = System.nanoTime();
            byte[] encryptedRSA1024 = encryptRSA(fileContent, keyPair1024.getPublic());
            endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append(",");

            startTime = System.nanoTime();
            decryptRSA(encryptedRSA1024, keyPair1024.getPrivate());
            endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append(",");

            // RSA 2048
            startTime = System.nanoTime();
            byte[] encryptedRSA2048 = encryptRSA(fileContent, keyPair2048.getPublic());
            endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append(",");

            startTime = System.nanoTime();
            decryptRSA(encryptedRSA2048, keyPair2048.getPrivate());
            endTime = System.nanoTime();
            csvContent.append((endTime - startTime) / 1_000_000.0).append("\n");
        }

        Files.write(Paths.get("performance_results.csv"), csvContent.toString().getBytes());
    }

    private static SecretKey generateDESKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating DES key", e);
        }
    }

    private static byte[] encryptDES(byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        return cipher.doFinal(input);
    }

    private static byte[] decryptDES(byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        return cipher.doFinal(input);
    }

    private static KeyPair generateRSAKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] encryptRSA(byte[] input, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int maxBlockSize = ((RSAPublicKey)publicKey).getModulus().bitLength() / 8 - 11;
        return cipher.doFinal(Arrays.copyOf(input, Math.min(input.length, maxBlockSize)));
    }

    private static byte[] decryptRSA(byte[] input, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(input);
    }

}