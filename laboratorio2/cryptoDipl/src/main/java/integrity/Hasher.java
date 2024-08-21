package integrity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import util.Util;

public class Hasher {

    public static String getHash(String input, String algorithm) throws Exception {
        byte[] inputBA = input.getBytes();
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        hasher.update(inputBA);
        return Util.byteArrayToHexString(hasher.digest(), "");
    }

    public static String getHashFile(String filename, String algorithm) throws Exception {
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        int in;
        while ((in = fis. read(buffer)) != -1) {
                hasher. update(buffer, 0, in);
    }
        fis.close();
        return Util.byteArrayToHexString(hasher.digest(),"");
    }

//    public static void generateIntegrityCheckFile(String directory, String insertName) throws IOException {
//        File dir = new File(directory);
//        if (!dir.exists()) {
//            throw new IOException("El directorio especificado no existe: " + directory);
//        }
//        if (!dir.isDirectory()) {
//            throw new IOException("La ruta especificada no es un directorio: " + directory);
//        }
//        File[] files = dir.listFiles();
//        if (files == null || files.length == 0) {
//            throw new IOException("El directorio está vacío: " + directory);
//        }
//        String outputPath = "src/main/resources/" + insertName;
//
//        StringBuilder contentBuilder = new StringBuilder();
//
//        for (File file : files) {
//            try {
//                String hash = getHashFile(file.getAbsolutePath(), "SHA-256");
//                String name = file.getName();
//                contentBuilder.append(hash).append("  ").append(name).append("\n");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        Files.write(Paths.get(outputPath), contentBuilder.toString().getBytes(),
//                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//
//        System.out.println("Archivo de verificación de integridad generado en " + outputPath);
//    }

    public static void generateIntegrityCheckerFile(String folderName, String fileExportName) throws Exception {
        FileWriter fw = new FileWriter(fileExportName);
        BufferedWriter bw = new BufferedWriter(fw);
        File folder = new File(folderName);
        if (folder.exists()){
            File[] files = folder.listFiles();
            if (files.length > 0){
                int i = 0;
                for (File file : files){
                    String hash = getHashFile(file.getPath(),"SHA-256");
                    hash += " *" + file.getName();
                    bw.write(hash);
                    i++;
                    if(i < files.length){
                        bw.newLine();
                    }
                }
            } else {
                throw new Exception("No existen archivos en la carpeta");
            }
        } else {
            throw new Exception("No existe la carpeta");
        }
        bw.close();
    }

    public static void checkIntegrityFile(String folderName, String fileExportName, String ignored) throws Exception {
        FileReader fr = new FileReader(fileExportName);
        BufferedReader br = new BufferedReader(fr);
        String[] lines = br.lines().toArray(String[]::new);
        File folder = new File(folderName);
        int contFailed = 0;
        int contFile = 0;
        if (folder.exists()){
            File[] files = folder.listFiles();
            if (files.length > 0){
                for (int i = 0; i < lines.length; i++){
                    String hashTxt = lines[i].split(" ")[0];
                    String nombreTxt = lines[i].split(" ")[1].substring(1);
                    boolean existe = false;
                    for (File file : files){
                        if(!file.getName().equals(ignored)){
                            if(nombreTxt.equals(file.getName())){
                                String hashArchivo = getHashFile(file.getPath(),"SHA-256");
                                if (hashArchivo.equals(hashTxt)){
                                    System.out.println(file.getName()+": OK");
                                }else{
                                    System.out.println(file.getName()+": FAILED");
                                    contFailed++;
                                }
                                existe = true;
                            }
                        }
                    }
                    if(!existe){
                        if (ignored == null){
                            System.out.println(nombreTxt + ": No such file or directory");
                            System.out.println(nombreTxt + ": FAILED open or read");
                            contFile++;
                        }
                    }
                }
            } else {
                throw new Exception("No existen archivos en la carpeta");
            }
        } else {
            throw new Exception("No existe la carpeta");
        }

        if (contFile == 1) {
            System.out.println("shasum: WARNING: 1 listed file could not be read");
        } else if (contFile > 1) {
            System.out.println("shasum: WARNING: " + contFile + " listed files could not be read");
        }

        if (contFailed == 1) {
            System.out.println("shasum: WARNING: 1 computed checksum did NOT match");
        } else if (contFailed > 1) {
            System.out.println("shasum: WARNING: " + contFailed + " computed checksums did NOT match");
        }
    }

}

