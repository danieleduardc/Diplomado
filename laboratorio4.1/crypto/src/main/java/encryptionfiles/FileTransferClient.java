package encryptionfiles;


import util.fileEncryptionAllinOne;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class FileTransferClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String keyFilePath = "secret.key";
            String inputFilePath = "scan.pdf";
            String encryptedFilePath = inputFilePath + ".encrypted";
            String hashFilePath = encryptedFilePath + ".hash";

            // Generar y guardar la llave secreta
            fileEncryptionAllinOne.generateAndSaveKey(keyFilePath);

            // Encriptar el archivo binario
            fileEncryptionAllinOne.encryptFile(inputFilePath, keyFilePath);
            System.out.println("Archivo binario encriptado correctamente.");

            // Generar hash del archivo encriptado
            generateFileHash(encryptedFilePath, hashFilePath);

            // Enviar archivo encriptado
            sendFile(encryptedFilePath, dos);

            // Enviar llave
            sendFile(keyFilePath, dos);

            // Enviar hash
            sendFile(hashFilePath, dos);

            // Leer respuesta del servidor
            String response = dis.readUTF();
            System.out.println("Respuesta del servidor: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateFileHash(String filePath, String hashFilePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = Files.newInputStream(Paths.get(filePath))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        byte[] hashBytes = digest.digest();
        String fileHash = Base64.getEncoder().encodeToString(hashBytes);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(hashFilePath))) {
            writer.write(fileHash);
        }
    }

    private static void sendFile(String filePath, DataOutputStream dos) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        dos.writeUTF(file.getName());
        dos.writeLong(fileSize);
        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(file)) {
            int read;
            while ((read = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }
        }
    }
}
