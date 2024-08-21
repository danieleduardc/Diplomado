package integrity;

public class HasherTester2 {

    public static void main(String[] args) throws Exception {
        String fileName = "CarpetaRecibida/integrity_check.txt";
        String hash = Hasher.getHashFile(fileName, "SHA-256");
        System.out.println(hash);

    }

}