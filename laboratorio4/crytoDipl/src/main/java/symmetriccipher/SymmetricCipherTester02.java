package symmetriccipher;


import util.Util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricCipherTester02 {

    public static void main(String[] args) throws Exception {
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        String clearText = "In symmetric key cryptography, the same key is used " +
                "to encrypt and decrypt the clear text.";
        System.out.println(clearText);

        byte[] encryptedText = cipher.encryptMessage(clearText);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));

        Util.saveObject(secretKey, "secretKey.key");
        System.out.println("The secret key has been saved");

        Util.saveObject(encryptedText, "text.encrypted");
        System.out.println("The encrypted text has been saved");
    }

}
