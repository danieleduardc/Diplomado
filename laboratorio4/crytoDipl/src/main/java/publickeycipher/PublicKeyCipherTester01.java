package publickeycipher;

import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester01 {

    public static void main(String[] args) throws Exception {

        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKeyCipher cipher = new PublicKeyCipher(algorithm);

        String clearText = " This is a secret message";
        System.out.println(clearText);

        byte[] encryptedText = cipher.encryptMessage(clearText, publicKey);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));

        clearText = cipher.decryptMessage(encryptedText, privateKey);
        System.out.println(clearText);

        encryptedText = cipher.encryptMessage(clearText, privateKey);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));
        clearText = cipher.decryptMessage(encryptedText, publicKey);
        System.out.println(clearText);


    }

}
