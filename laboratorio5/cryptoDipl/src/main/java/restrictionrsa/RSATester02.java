package restrictionrsa;

import util.Util;

public class RSATester02 {
    public static void main(String[] args) {

        byte[] testData = "Este es un mensaje de prueba para dividir en fragmentos".getBytes();
        int fragmentSize = 7;
        byte[][] fragments = Util.splitByteArray(testData, fragmentSize);

        System.out.println("\nEjemplo de división de bytes:");
        System.out.println("Longitud del byte[]: " + testData.length);
        System.out.println("Tamaño del fragmento: " + fragmentSize);
        System.out.println("Cantidad de fragmentos: " + fragments.length);
        System.out.println("Tamaño del último fragmento: " + fragments[fragments.length - 1].length);

        for (int i = 0; i < fragments.length; i++) {
            System.out.println("Fragmento " + (i + 1) + ": " + new String(fragments[i]));
        }
    }
}
