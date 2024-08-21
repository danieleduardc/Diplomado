package symmetriccipher;

import util.Util;

import javax.crypto.SecretKey;

public class SymmetricCipherTester08 {
    public static void main(String[] args) throws Exception{

        SecretKey secretKey = (SecretKey) Util.loadObject("secretKey.key");
        SymmetricCipher cipher = new SymmetricCipher (secretKey, "DES/ECB/PKCS5Padding");

        cipher.decryptFile("scan.pdf.encrypted");


    }
}
