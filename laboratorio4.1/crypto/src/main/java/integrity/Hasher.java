package integrity;

import util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.List;

public class Hasher {

    public static String getHash(String input, String algorithm) throws Exception {
        byte[] inputBA = input.getBytes();
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        hasher. update(inputBA) ;
        return Util.byteArrayToHexString(hasher.digest(),"");
    }

    public static String getHashFile(String filename, String algorithm) throws Exception {
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        int in;
        while ((in = fis.read(buffer)) != -1) {
            hasher.update(buffer, 0, in);
        }
        fis.close();
        return Util.byteArrayToHexString(hasher.digest(),"");
    }
    
 // Método para generar el archivo de integridad
    public static void generateIntegrityCheckerFile(String folderName, String outputFileName) throws Exception {
        File folder = new File(folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("La carpeta no existe o no es un directorio.");
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("La carpeta está vacía.");
        }

        File outputFile = new File(folderName, outputFileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (File file : files) {
                if (file.isFile()) {
                    String hash = getHashFile(file.getPath(), "SHA-256");
                    writer.write(hash + " *" + file.getName());
                    writer.newLine();
                }
            }
        }
    }

    // Método para verificar la integridad de los archivos
    public static void checkIntegrityFile(String folderName, String integrityFileName) throws Exception {
    File folder = new File(folderName);
    if (!folder.exists() || !folder.isDirectory()) {
        throw new IllegalArgumentException("La carpeta no existe o no es un directorio.");
    }

    File integrityFile = new File(folderName, integrityFileName);
    if (!integrityFile.exists()) {
        throw new IllegalArgumentException("El archivo de integridad no existe.");
    }

    List<String> lines = Files.readAllLines(integrityFile.toPath());
    int failedCount = 0;

    for (String line : lines) {
        String[] parts = line.split(" \\*");
        String expectedHash = parts[0];
        String fileName = parts[1];

        File file = new File(folderName + "/" + fileName);
        if (file.exists()) {
            String actualHash = getHashFile(file.getPath(), "SHA-256");
            if (expectedHash.equals(actualHash)) {
                System.out.println(fileName + ": OK");
            } else {
                System.out.println(fileName + ": Fallo");
                failedCount++;
            }
        } else {
            System.out.println(fileName + ": No encontrado");
            failedCount++;
        }
    }

    if (failedCount == 0) {
        System.out.println("Todos los archivos son correctos.");
    } else {
        System.out.println(failedCount + " archivo(s) fallo.");
    }
}

}
