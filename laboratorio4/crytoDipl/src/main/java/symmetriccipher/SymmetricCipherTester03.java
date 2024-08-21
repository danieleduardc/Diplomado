package symmetriccipher;

import util.Util;

import javax.crypto.SecretKey;

public class SymmetricCipherTester03 {

    public static void main(String[] args) throws Exception {

        SecretKey secretKey = (SecretKey) Util.loadObject("secretKey.key");
        System.out.println("La llave simétrica ha sido recuperada");

        byte[] encryptedText = (byte[]) Util.loadObject("text.encrypted");
        System.out.println("El texto cifrado ha sido recuperado");

        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        String clearText2 = cipher.decryptMessage(encryptedText);
        System.out .println(clearText2);
    }

}
