package restrictionrsa;

import util.Util;

import java.util.Arrays;

public class RSATester03 {
    public static void main(String[] args) {

        String testMessage = "Este es un mensaje de prueba para dividir y luego unir.";
        byte[] originalData = testMessage.getBytes();
        int fragmentSize = 10;

        System.out.println("Mensaje original: " + testMessage);
        System.out.println("Longitud original: " + originalData.length + " bytes");
        System.out.println("Tamaño de fragmento: " + fragmentSize + " bytes");

        // Dividir el mensaje
        byte[][] fragments = Util.splitByteArray(originalData, fragmentSize);
        System.out.println("Número de fragmentos: " + fragments.length);

        System.out.println("\nFragmentos:");
        for (int i = 0; i < fragments.length; i++) {
            System.out.println("Fragmento " + (i + 1) + ": " + new String(fragments[i]));
        }

        // Unir los fragmentos
        byte[] joinedData = Util.joinByteArray(fragments);
        String joinedMessage = new String(joinedData);

        System.out.println("\nMensaje unido: " + joinedMessage);
        System.out.println("Longitud después de unir: " + joinedData.length + " bytes");

        // Verificar si el mensaje original y el unido son iguales
        boolean isEqual = Arrays.equals(originalData, joinedData);
        System.out.println("¿El mensaje unido es igual al original? " + isEqual);
    }
}