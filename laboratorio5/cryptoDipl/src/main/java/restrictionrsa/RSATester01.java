package restrictionrsa;

public class RSATester01 {
    public static void main(String[] args) {

        int[] keySizes = {1024, 2048, 3072, 4096};

        for (int keySize : keySizes) {
            try {
                int maxSize = RSAEncryption.findMaxEncryptionSize(keySize);
                System.out.println("Para una llave RSA de " + keySize + " bits, el tamaño máximo de datos que se puede encriptar es: " + maxSize + "bytes");
            } catch (Exception e) {
                System.out.println("Error al probar con llave de " + keySize + " bits: " + e.getMessage());
            }
        }
    }
}
