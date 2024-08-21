package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Util {

    public static String byteArrayToHexString(byte[] bytes, String separator) {
        String result = "";
        for (int i=0; i<bytes. length; i++) {
                result += String. format("%02x", bytes[i]) + separator;
        }
        return result.toString();
    }

    
    public static void saveObject(Object o, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(o);
        }
    }

    public static Object loadObject(String fileName) throws IOException, ClassNotFoundException, InterruptedException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Thread.sleep(100);
            return in.readObject();
        }
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    public static byte[] objectToByteArray(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.close();
        return bos.toByteArray();
    }

    public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = new ObjectInputStream(bis);
        Object o = in.readObject();
        in.close();
        return o;
    }

  
}
