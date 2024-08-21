package util;

import java.io.*;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

public class Util {

    public static String byteArrayToHexString(byte[] bytes, String separator) {
        String result = "";
        for (int i=0; i<bytes. length; i++) {
            result += String. format("%02x", bytes[i]) + separator;
        }
        return result.toString();
    }

    public static void saveObject (Object o, String fileName) throws IOException {
        FileOutputStream fileOut;
        ObjectOutputStream out;
        fileOut = new FileOutputStream(fileName);
        out = new ObjectOutputStream(fileOut);
        out.writeObject(o);
        out.flush();
        out.close();
    }

    public static Object loadObject (String fileName) throws Exception {
        FileInputStream fileIn;
        ObjectInputStream in;
        fileIn = new FileInputStream(fileName);
        in = new ObjectInputStream(fileIn);
        Thread .sleep(100);
        Object o = in.readObject();
        fileIn.close();
        in. close();
        return o;
    }

    public static byte[] objectToByteArray(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.close();
        byte[] buffer = bos.toByteArray();
        return buffer;
    }

    public static Object byteArrayToObject(byte[] byteArray) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        Object o = in.readObject();
        in. close();
        return o;
    }
    public static byte[][] splitByteArray(byte[] input, int fragmentSize) {
        int totalFragments = (int) Math.ceil((double) input.length / fragmentSize);
        byte[][] result = new byte[totalFragments][];

        for (int i = 0; i < totalFragments; i++) {
            int start = i * fragmentSize;
            int length = Math.min(fragmentSize, input.length - start);
            result[i] = Arrays.copyOfRange(input, start, start + length);
        }

        return result;
    }

    public static byte[] joinByteArray(byte[][] input) {
        int totalLength = 0;
        for (byte[] fragment : input) {
            totalLength += fragment.length;
        }

        byte[] result = new byte[totalLength];
        int currentPosition = 0;

        for (byte[] fragment : input) {
            System.arraycopy(fragment, 0, result, currentPosition, fragment.length);
            currentPosition += fragment.length;
        }

        return result;
    }

    public static String printKey(String tipeKey, Key key) throws Exception {
        String result = "-----BEGIN " + tipeKey + " KEY-----\n";
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        result += encodedKey;
        result += "\n-----END " + tipeKey + " KEY-----\n";
        return result;
    }


}
