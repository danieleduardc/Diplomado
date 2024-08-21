package integrity;

public class HasherTester3 {

    public static void main(String[] args) throws Exception {
        //Hasher.generateIntegrityCheckerFile("binarios" , "prueba.txt");
        Hasher.checkIntegrityFile("binarios" , "prueba.txt", null);
    }

}
