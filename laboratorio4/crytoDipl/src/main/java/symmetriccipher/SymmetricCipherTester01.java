package symmetriccipher;

import util.Util;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricCipherTester01 {

    public static void main(String[] args) throws Exception{
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymmetricCipher cipher = new SymmetricCipher (secretKey, "DES/ECB/PKCS5Padding");

        String clearText = "In symmetric key cryptography, the same key is used " +
                "to encrypt and decrypt the clear text.";
        System.out .println(clearText);

        byte[] encryptedText = cipher.encryptMessage(clearText);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));

        String clearText2 = cipher.decryptMessage(encryptedText);
        System.out .println(clearText2);
    }

}
