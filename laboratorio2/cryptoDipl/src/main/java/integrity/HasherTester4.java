package integrity;

public class HasherTester4 {

        public static void main(String args[]) throws Exception {

            Hasher.checkIntegrityFile("src/binary", "hash.txt", null);
        }
}
