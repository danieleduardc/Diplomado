package publickeycipher;

import java.nio.file.Files;

import javax.crypto.Cipher;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyCipherTester04 {

    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";

        String base64PublicKey = new String(Files.readAllBytes(Paths.get("publickey.txt")));
        String base64PrivateKey = new String(Files.readAllBytes(Paths.get("privatekey.txt")));

        byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
        byte[] privateKeyBytes = Base64.getDecoder().decode(base64PrivateKey);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        String encryptedMessageBase64 = new String(Files.readAllBytes(Paths.get("mensaje.txt")));
        byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedMessageBase64);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes);

        String decryptedMessage = new String(decryptedMessageBytes);
        System.out.println(decryptedMessage);

    }

}
