package util;

import java.io.*;
import java.net.Socket;
import integrity.Hasher;

public class Files {

    public static void sendFile(String filename, Socket socket) throws Exception {
    System.out.println("File to send: " + filename);
    File localFile = new File(filename);
    BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

    // Send the name of the file
    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
    printWriter.println(localFile.getName()); // Enviar solo el nombre del archivo

    // Send the size of the file (in bytes)
    long size = localFile.length();
    printWriter.println("Size:" + size);

    BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

    pause(50);
    
    // Send the file content
    byte[] blockToSend = new byte[1024];
    int in;
    while ((in = fromFile.read(blockToSend)) != -1) {
        toNetwork.write(blockToSend, 0, in);
    }
    toNetwork.flush();
    fromFile.close();

    pause(50);
}

    
    public static String receiveFile(String folder, Socket socket) throws Exception {
    File fd = new File(folder);
    if (!fd.exists()) {
        fd.mkdir();
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

    // Receive the name of the file
    String filename = reader.readLine();
    if (filename == null) {
        throw new IOException("Filename not received");
    }
    filename = folder + File.separator + filename;

    BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream(filename));

    System.out.println("File to receive: " + filename);

    // Receive the size of the file
    String sizeString = reader.readLine();
    if (sizeString == null) {
        throw new IOException("File size not received");
    }

    long size = Long.parseLong(sizeString.split(":")[1]);
    System.out.println("Size: " + size);

    // Receive the file content
    byte[] blockToReceive = new byte[512];
    int in;
    long remainder = size;
    while ((in = fromNetwork.read(blockToReceive)) != -1) {
        toFile.write(blockToReceive, 0, in);
        remainder -= in;
        if (remainder == 0) {
            break;
        }
    }

    pause(50);
    
    toFile.flush();
    toFile.close();
    System.out.println("File received: " + filename);

    return filename;
}

    
    public static void sendFolder(String folderName, Socket socket) throws Exception {
    File folder = new File(folderName);
    if (!folder.exists() || !folder.isDirectory()) {
        throw new IllegalArgumentException("La carpeta no existe o no es un directorio.");
    }

    File[] files = folder.listFiles();
    if (files == null || files.length == 0) {
        throw new IllegalArgumentException("La carpeta está vacía.");
    }

    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
    printWriter.println(files.length); // Enviar el número de archivos

    for (File file : files) {
        if (file.isFile()) {
            sendFile(file.getPath(), socket);
        }
    }

    // Generar y enviar el archivo de integridad
    String integrityFileName = "integrity_check.txt";
    Hasher.generateIntegrityCheckerFile(folderName, integrityFileName);
    //String veo = folderName + File.separator + integrityFileName;
    //System.out.println("Ruta del integrity: "+ veo);
    sendFile(folderName + "/" + integrityFileName, socket);
}


    public static void receiveFolder(String folderName, Socket socket) throws Exception {
    File folder = new File(folderName);
    if (!folder.exists()) {
        folder.mkdir();
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    int fileCount = Integer.parseInt(reader.readLine()); // Recibir el número de archivos

    for (int i = 0; i < fileCount; i++) {
        receiveFile(folderName, socket);
    }

    // Recibir y verificar el archivo de integridad
    String integrityFileName = receiveFile(folderName, socket);
    System.out.println("Lo que envia al verificar: "+ folderName +" | "+ integrityFileName);
    Hasher.checkIntegrityFile(folderName, "integrity_check.txt");
}
    
    public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}

}
