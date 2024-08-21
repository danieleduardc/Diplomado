package integrity;

public class HasherTester3 {

    public static void main(String[] args) throws Exception {
      // Hasher.generateIntegrityCheckerFile("Binary" , "zhash.txt");
       Hasher.checkIntegrityFile("Binary" , "zhash.txt", null);
    }

}
