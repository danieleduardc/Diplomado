package base64;

import util.Base64;
import util.Util;

import java.util.ArrayList;

public class Base64Tester02 {

    public static void main(String[] args) throws Exception {
        Person person1 = new Person("Daniel", 22, 1.72);
        System.out.println(person1.toString());

        byte[] personByte = Util.objectToByteArray(person1);
        String personB64 = Base64.encode(personByte);
        System.out.println(personB64);

        byte[] personByte2 = Base64.decode(personB64);
        Person person2 = (Person) Util.byteArrayToObject(personByte2);
        System.out.println(person2.toString());
    }

}
