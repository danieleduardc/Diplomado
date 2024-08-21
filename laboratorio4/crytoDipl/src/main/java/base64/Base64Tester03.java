package base64;

import util.Base64;
import util.Util;

import java.util.ArrayList;

public class Base64Tester03 {

    public static void main(String[] args) throws Exception {

        ArrayList<Person> people = new ArrayList<Person>();
        people.add(new Person("Valentina", 19, 1.65));
        people.add(new Person("Daniel", 22, 1.72));
        people.add(new Person("Esteban", 23, 1.70));
        people.add(new Person("Nicolas", 24, 1.80));

        System.out.println(people);

        byte[] peoples = Util.objectToByteArray(people);
        String peoplesB64 = Base64.encode(peoples);
        System.out.println(peoplesB64);

        byte[] peoplesBA2 = Base64. decode(peoplesB64);
        ArrayList<Person> people2 = (ArrayList<Person>) Util.byteArrayToObject(peoplesBA2);
        System.out.println(people2);

    }

}
