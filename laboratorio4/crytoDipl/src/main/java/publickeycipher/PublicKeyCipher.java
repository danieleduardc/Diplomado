package publickeycipher;

import util.Util;

import javax.crypto.Cipher;
import java.security.Key;

public class PublicKeyCipher {

    private Cipher cipher;

    public PublicKeyCipher(String algorithm) throws Exception{
        cipher = Cipher.getInstance(algorithm);
    }

    public byte[] encryptMessage(String input, Key key) throws Exception {
        byte[] cipherText = null;
        byte[] clearText = input.getBytes();

        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(clearText);
        return cipherText;
    }

    public String decryptMessage(byte[] input, Key key) throws Exception {
        String output = null;
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] clearText = cipher.doFinal(input);
        output =  new String(clearText);

        return output;
    }

    public byte[] encryptObject( Object input, Key key) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] clearObject = Util.objectToByteArray(input);
        byte[] cipherObject = cipher.doFinal(clearObject);
        return cipherObject;
    }

    public Object decryptObject(byte[] input, Key key) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] clearText = cipher.doFinal(input);
        Object output = Util.byteArrayToObject(clearText);

        return output;
    }

}
