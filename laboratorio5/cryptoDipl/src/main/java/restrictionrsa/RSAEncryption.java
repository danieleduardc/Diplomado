package restrictionrsa;

import util.Base64;
import util.Util;

import java.security.*;
import javax.crypto.Cipher;
import java.util.Random;

public class RSAEncryption {

    public static String generateRandomString(int length) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder cadena = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            cadena.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return cadena.toString();
    }

    public static int findMaxEncryptionSize(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

        int maxSize = 0;
        while (true) {
            try {
                String testString = generateRandomString(maxSize + 1);
                cipher.doFinal(testString.getBytes());
                maxSize++;
            } catch (Exception e) {
                break;
            }
        }

        return maxSize;
    }


    public static byte[] encryptLargeContent(byte[] content, PublicKey publicKey, int maxBlockSize) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[][] blocks = Util.splitByteArray(content, maxBlockSize);
        byte[][] encryptedBlocks = new byte[blocks.length][];

        for (int i = 0; i < blocks.length; i++) {
            encryptedBlocks[i] = cipher.doFinal(blocks[i]);
        }

        return Util.joinByteArray(encryptedBlocks);
    }

    public static byte[] decryptLargeContent(byte[] encryptedContent, PrivateKey privateKey, int encryptedBlockSize) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[][] encryptedBlocks = Util.splitByteArray(encryptedContent, encryptedBlockSize);
        byte[][] decryptedBlocks = new byte[encryptedBlocks.length][];

        for (int i = 0; i < encryptedBlocks.length; i++) {
            decryptedBlocks[i] = cipher.doFinal(encryptedBlocks[i]);
        }

        return Util.joinByteArray(decryptedBlocks);
    }
}
