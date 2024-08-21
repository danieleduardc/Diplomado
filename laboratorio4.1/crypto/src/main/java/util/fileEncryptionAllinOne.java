package util;



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

public class fileEncryptionAllinOne {
    private static final String ALGORITHM = "DES";
    private static final String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    public static void generateAndSaveKey(String keyFilePath) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        SecretKey secretKey = keyGen.generateKey();
        try (FileOutputStream keyOut = new FileOutputStream(keyFilePath)) {
            keyOut.write(secretKey.getEncoded());
        }
    }

    public static SecretKey loadKey(String keyFilePath) throws Exception {
        byte[] keyBytes = new byte[8]; // DES key length is 8 bytes
        try (FileInputStream keyIn = new FileInputStream(keyFilePath)) {
            keyIn.read(keyBytes);
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void encryptFile(String inputFilePath, String keyFilePath) throws Exception {
        SecretKey secretKey = loadKey(keyFilePath);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath + ".encrypted"))) {
            byte[] buffer = new byte[8]; // Block size for DES is 8 bytes
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    String encrypted = Base64.getEncoder().encodeToString(output);
                    writer.write(encrypted);
                    writer.newLine();
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                String encrypted = Base64.getEncoder().encodeToString(output);
                writer.write(encrypted);
                writer.newLine();
            }
        }
    }

    public static void decryptFile(String encryptedFilePath, String keyFilePath) throws Exception {
        SecretKey secretKey = loadKey(keyFilePath);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        String plainFilePath = encryptedFilePath.replace(".encrypted", ".plain");

        try (BufferedReader reader = new BufferedReader(new FileReader(encryptedFilePath));
             FileOutputStream fos = new FileOutputStream(plainFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] encryptedBytes = Base64.getDecoder().decode(line);
                byte[] decryptedBytes = cipher.update(encryptedBytes);
                if (decryptedBytes != null) {
                    fos.write(decryptedBytes);
                }
            }
            byte[] decryptedBytes = cipher.doFinal();
            if (decryptedBytes != null) {
                fos.write(decryptedBytes);
            }
        }
    }
}
