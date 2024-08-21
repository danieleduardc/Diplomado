package integrity;

public class HasherTester2 {

    public static void main(String[] args) throws Exception {
        String fileName = "CHIRIPAS.pdf";
        String hash = Hasher.getHashFile(fileName, "SHA-256");
        System.out.println(hash);

    }

}
