package restrictionrsa;

import util.Base64;
import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;

public class RSATester04 {
    public static void main(String[] args) {

        try {
            // Generar par de claves RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Contenido a cifrar (mayor que el tamaño permitido por RSA)
            String largeContent = "Este es un contenido de prueba que es más largo que el tamaño máximo permitido por RSA. " +
                    "Vamos a cifrarlo dividiéndolo en bloques más pequeños, cifrando cada bloque por separado, " +
                    "y luego uniendo los resultados. Después, haremos el proceso inverso para descifrar y " +
                    "recuperar el contenido original.";

            System.out.println("Contenido original:");
            System.out.println(largeContent);
            System.out.println("Longitud del contenido: " + largeContent.getBytes().length + " bytes");

            // Encontrar el tamaño máximo de bloque para cifrar
            int maxBlockSize = RSAEncryption.findMaxEncryptionSize(2048) - 11;  // 11 bytes para padding PKCS1
            System.out.println("Tamaño máximo de bloque para cifrar: " + maxBlockSize + " bytes");

            // Cifrar el contenido
            byte[] encryptedContent = RSAEncryption.encryptLargeContent(largeContent.getBytes(), keyPair.getPublic(), maxBlockSize);
            System.out.println("\nContenido cifrado:");
            System.out.println(Base64.encode(encryptedContent));
            System.out.println("Longitud del contenido cifrado: " + encryptedContent.length + " bytes");

            // Descifrar el contenido
            byte[] decryptedContent = RSAEncryption.decryptLargeContent(encryptedContent, keyPair.getPrivate(), 256);  // 256 bytes para RSA 2048 bits
            String decryptedString = new String(decryptedContent);

            System.out.println("\nContenido descifrado:");
            System.out.println(decryptedString);
            System.out.println("Longitud del contenido descifrado: " + decryptedContent.length + " bytes");

            // Verificar si el contenido original y el descifrado son iguales
            boolean istEqual = largeContent.equals(decryptedString);
            System.out.println("\n¿El contenido descifrado es igual al original? " + istEqual);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}