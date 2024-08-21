package publickeycipher;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class PublicKeyCipherTester03 {

    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        PublicKeyCipher cipher = new PublicKeyCipher(algorithm);

        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        Files.write(Paths.get("publickey.txt"), base64PublicKey.getBytes());
        Files.write(Paths.get("privatekey.txt"), base64PrivateKey.getBytes());

        String clearText = "This is a secret messages";

        Cipher cipherr = Cipher.getInstance("RSA");
        cipherr.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessageBytes = cipherr.doFinal(clearText.getBytes());

        String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        Files.write(Paths.get("mensaje.txt"), encryptedMessageBase64.getBytes());

    }

}
