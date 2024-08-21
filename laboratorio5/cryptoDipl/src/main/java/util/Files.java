package util;

import integrity.Hasher;

import java.io.*;
import java.net.Socket;

public class Files {

	public static void sendFolder(String folderName, Socket socket) throws Exception {
		File folder = new File(folderName);
		if (folder.exists()) {
			Hasher.generateIntegrityCheckerFile("binarios" , "binarios/check.txt");
			File[] files = folder.listFiles();
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("folderName:binariosrecibidos");
			printWriter.println("name:check.txt");
			printWriter.println("cant:" + files.length);
			BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());
			if (files.length > 0) {

				for (File file : files){
					sendFile(file.getPath(), file.getName(), socket);
				}
			}
		}
	}

	public static void receiveFolder(String folderName, Socket socket) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());
		folderName = reader.readLine();
		folderName = folderName.split(":")[1];
		String validate = reader.readLine();
		validate = validate.split(":")[1];
		String sizeString = reader.readLine();
		long size = Long.parseLong(sizeString.split(":")[1]);

		for (int i = 0; i < size; i++) {
			Files.receiveFile(folderName, socket);
		}

		//revisar integridad

		Hasher.checkIntegrityFile(folderName , folderName + "/" + validate , validate );


		System.out.println("carpeta: " + folderName);
		System.out.println("cant: " + size);

	}

	public static void sendFile(String filename, String name,  Socket socket) throws Exception {
		System.out.println("File to send: " + filename);
		File localFile = new File(filename);
		BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

		// send the size of the file (in bytes)
		long size = localFile.length();
		System.out.println("Size: " + size);

		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println(name);

		printWriter.println("Size:" + size);

		BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

		pause(50);
		
		// the file is sent one block at a time
		byte[] blockToSend = new byte[1024];
		int in;
		while ((in = fromFile.read(blockToSend)) != -1) {
			toNetwork.write(blockToSend, 0, in);
		}
		// the stream linked to the socket is flushed and closed
		toNetwork.flush();
		fromFile.close();

		pause(50);
	}

	public static String receiveFile(String folder, Socket socket) throws Exception {
		File fd = new File (folder);
		if (fd.exists()==false) {
			fd.mkdir();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

		String filename = reader.readLine();
		filename = folder + File.separator + filename;

		BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream(filename));

		System.out.println("File to receive: " + filename);

		String sizeString = reader.readLine();

		// the sender sends "Size:" + size, so here it is separated
		// long size = Long.parseLong(sizeString.subtring(5));
		long size = Long.parseLong(sizeString.split(":")[1]); 
		System.out.println("Size: " + size);
		
		// the file is received one block at a time
		byte[] blockToReceive = new byte[512];
		int in;
		long remainder = size; // lo que falta
		while ((in = fromNetwork.read(blockToReceive)) != -1) {
			toFile.write(blockToReceive, 0, in);
			remainder -= in;
			if (remainder == 0)
				break;
		}

		pause(50);
		
		// the stream linked to the file is flushed and closed
		toFile.flush();
		toFile.close();
		System.out.println("File received: " + filename);

		return filename;
	}

	public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}
}
