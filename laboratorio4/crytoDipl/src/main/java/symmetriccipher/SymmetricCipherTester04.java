package symmetriccipher;

import util.Util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.ArrayList;

public class SymmetricCipherTester04 {

    public static void main(String[] args) throws Exception {
        SecretKey secretKey = null;
        secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");
        ArrayList<String> clearObject = new ArrayList<String>();
        byte[] encrypedobject = null;
        clearObject.add("Ana");
        clearObject.add ("Bety");
        clearObject. add( "Carolina");
        clearObject. add( "Daniela");
        clearObject.add( "Elena");
        System.out.println(clearObject);
        encrypedobject = cipher.encryptobject(clearObject);
        System.out.println(Util.byteArrayToHexString(encrypedobject, " "));
        clearObject = (ArrayList<String>) cipher.decryptoObject(encrypedobject);
        System.out.println(clearObject);
    }

}
