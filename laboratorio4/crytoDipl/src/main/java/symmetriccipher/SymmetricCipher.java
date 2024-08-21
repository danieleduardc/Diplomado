package symmetriccipher;

import util.Base64;
import util.Util;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricCipher {

    private SecretKey secretKey;
    private Cipher cipher;

    public SymmetricCipher (SecretKey secretKey, String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.secretKey = secretKey;
        cipher = Cipher.getInstance(transformation);
    }

    public byte[] encryptMessage (String input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] clearText = input.getBytes();
        byte[] cipherText = null;
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipherText = cipher.doFinal(clearText) ;
        return cipherText;
    }

    public String decryptMessage(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearText = cipher. doFinal(input) ;
        output = new String(clearText);
        return output;
    }

    public byte[] encryptobject(Object input) throws Exception {
        byte[] cipherObject = null;
        byte[] clearObject = null;
        clearObject = Util.objectToByteArray(input);
        cipher.init(Cipher. ENCRYPT_MODE, secretKey);
        cipherObject = cipher.doFinal (clearObject);
        return cipherObject;
    }

    public Object decryptoObject(byte[] input) throws Exception {
        Object output = null;
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearObject = cipher.doFinal(input);
        output = Util.byteArrayToObject(clearObject);
        return output;
    }

    public void encryptTextFile(String fileToEncode) throws Exception {
        FileReader fr = new FileReader(fileToEncode);
        BufferedReader br = new BufferedReader(fr);
        String[] lines = br.lines().toArray(String[]::new);
        FileWriter fw = new FileWriter(fileToEncode + ".encrypted");
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < lines.length; i++) {
            byte[] encryptedText = encryptMessage(lines[i]);
            String encodeB64 = Base64.encode(encryptedText);
            bw.write(encodeB64);
            bw.newLine();
        }
        bw.close();
    }

    public void decryptTextFile(String fileToDecode) throws Exception {
        FileReader fr = new FileReader(fileToDecode);
        BufferedReader br = new BufferedReader(fr);
        String[] lines = br.lines().toArray(String[]::new);
        String nombre = fileToDecode.split("\\.")[0];
        FileWriter fw = new FileWriter(nombre + ".plain.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < lines.length; i++) {
            byte[] decodeB64 = Base64.decode(lines[i]);
            String decryptedText = decryptMessage(decodeB64);
            bw.write(decryptedText);
            bw.newLine();
        }
        bw.close();
    }

    public void encryptFile(String inputFilePath) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(inputFilePath + ".encrypted"))) {
            byte[] buffer = new byte[64]; // Block size for DES is 64 bits (8 bytes)
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    String encrypted = Base64.encode(output);
                    bos.write(encrypted.getBytes());
                    bos.write("\n".getBytes());
                }
            }
            byte[] output = cipher.doFinal();
            if (output != null) {
                String encrypted = Base64.encode(output);
                bos.write(encrypted.getBytes());
                bos.write("\n".getBytes());
            }
        }
    }

    public void decryptFile(String encryptedFilePath) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String nombre = encryptedFilePath.split("\\.")[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(encryptedFilePath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nombre + ".plain.pdf"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] encryptedBytes = Base64.decode(line);
                byte[] decryptedBytes = cipher.update(encryptedBytes);
                if (decryptedBytes != null) {
                    bos.write(decryptedBytes);
                }
            }
            byte[] decryptedBytes = cipher.doFinal();
            if (decryptedBytes != null) {
                bos.write(decryptedBytes);
            }
        }
    }
}
