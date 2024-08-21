package symmetriccipher;

import util.Util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricCipherTester07 {

    public static void main(String[] args) throws Exception{
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymmetricCipher cipher = new SymmetricCipher (secretKey, "DES/ECB/PKCS5Padding");
        Util.saveObject(secretKey, "secretKey.key");
        cipher.encryptFile("scan.pdf");




    }
}
