package encryptionfiles;

import util.fileEncryptionAllinOne;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class FileTransferServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor esperando conexión...");
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Recibir archivo encriptado
            String encryptedFileName = dis.readUTF();
            long fileSize = dis.readLong();
            byte[] buffer = new byte[1024];
            try (FileOutputStream fos = new FileOutputStream(encryptedFileName)) {
                int read;
                while (fileSize > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fos.write(buffer, 0, read);
                    fileSize -= read;
                }
            }

            // Recibir llave
            String keyFileName = dis.readUTF();
            fileSize = dis.readLong();
            try (FileOutputStream fos = new FileOutputStream(keyFileName)) {
                int read;
                while (fileSize > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fos.write(buffer, 0, read);
                    fileSize -= read;
                }
            }

            // Recibir hash
            String hashFileName = dis.readUTF();
            fileSize = dis.readLong();
            try (FileOutputStream fos = new FileOutputStream(hashFileName)) {
                int read;
                while (fileSize > 0 && (read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                    fos.write(buffer, 0, read);
                    fileSize -= read;
                }
            }

            // Verificar integridad
            boolean isValid = verifyFileIntegrity(encryptedFileName, hashFileName);

            if (isValid) {
                dos.writeUTF("Archivo recibido correctamente y verificado.");
                fileEncryptionAllinOne.decryptFile(encryptedFileName, keyFileName);
                System.out.println("Archivo desencriptado correctamente.");
            } else {
                dos.writeUTF("Error en la verificación del archivo.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean verifyFileIntegrity(String filePath, String hashFilePath) throws Exception {
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

        String storedHash;
        try (BufferedReader br = new BufferedReader(new FileReader(hashFilePath))) {
            storedHash = br.readLine();
        }

        return fileHash.equals(storedHash);
    }
}

