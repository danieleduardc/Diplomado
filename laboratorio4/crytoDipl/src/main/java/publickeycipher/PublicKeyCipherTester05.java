package publickeycipher;

import util.Util;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class PublicKeyCipherTester05 {

    public static void main(String[] args) throws Exception {

        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKeyCipher cipher = new PublicKeyCipher(algorithm);

        ArrayList<String> clearObject = new ArrayList<String>();
        clearObject.add("Valentina");
        clearObject.add ("Daniel");
        clearObject. add( "Andres");
        clearObject. add( "Juan");
        clearObject.add( "Camilo");


        byte[] encryptedObject = cipher.encryptObject(clearObject, publicKey);
        System.out.println(Util.byteArrayToHexString(encryptedObject, " "));

        ArrayList<String> decrypt = (ArrayList<String>) cipher.decryptObject(encryptedObject, privateKey);
        System.out.println(decrypt);


    }

}
