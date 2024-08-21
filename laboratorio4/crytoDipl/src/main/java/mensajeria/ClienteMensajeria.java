package mensajeria;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Scanner;

public class ClienteMensajeria {
	public static final String SERVER = "localhost";
	public static final int PORT = 3400;

	private static final Scanner SCANNER = new Scanner(System.in);

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;

	private Socket clientSideSocket;

	private String server;
	private int port;

	public ClienteMensajeria() {
		this.server = SERVER;
		this.port = PORT;
	}

	public ClienteMensajeria(String server, int port) {
		this.server = server;
		this.port = port;
	}


	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void protocol(Socket socket) throws Exception {


		String algorithm = "RSA";
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();

		String base64PublicKey = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());

		boolean running = true;

		while (running) {
			String[] options = {"REGISTRAR", "OBTENER LLAVE PUBLICA", "ENVIAR", "LEER", "SALIR"};
			int choice = JOptionPane.showOptionDialog(
					null,
					"Seleccione una opción",
					"Menú",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[0]
			);

			String mensaje = "";
			String fromServer = "";
			ClienteMensajeria ec = null;
			ec = new ClienteMensajeria();
			switch (choice) {
				case 0:
					createStreams(socket);
					mensaje = "REGISTRAR etorresl_1@uqvirtual.edu.co " + base64PublicKey;
					toNetwork.println(mensaje);
					System.out.println("[Client] to server: " + mensaje);
					fromServer = fromNetwork.readLine();
					System.out.println("[Client] From server: " + fromServer);
					ec.init();
					System.out.println("[Client] Finished.");
					break;
				case 1:
					createStreams(socket);
					mensaje = "OBTENER_LLAVE_PUBLICA etorresl_1@uqvirtual.edu.co";
					toNetwork.println(mensaje);
					System.out.println("[Client] to server: " + mensaje);
					fromServer = fromNetwork.readLine();
					System.out.println("[Client] From server: " + fromServer);
					ec.init();
					break;
				case 2:
					createStreams(socket);
					String clearText = "hola como estas?";

					Cipher cipherr = Cipher.getInstance("RSA");
					cipherr.init(Cipher.ENCRYPT_MODE, publicKey);
					byte[] encryptedMessageBytes = cipherr.doFinal(clearText.getBytes());

					String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessageBytes);
					mensaje = "ENVIAR etorresl_1@uqvirtual.edu.co " + encryptedMessageBase64;
					toNetwork.println(mensaje);
					System.out.println("[Client] to server: " + mensaje);
					fromServer = fromNetwork.readLine();
					System.out.println("[Client] From server: " + fromServer);
					ec.init();
					break;
				case 3:
					createStreams(socket);
					mensaje = "LEER etorresl_1@uqvirtual.edu.co";
					toNetwork.println(mensaje);
					System.out.println("[Client] to server: " + mensaje);
					fromServer = fromNetwork.readLine();
					System.out.println("[Client] to server: " + fromServer);


					int n = Integer.parseInt(fromNetwork.readLine());

					for (int i = 0; i < n; i++) {
						String mensajeEncriptado = fromNetwork.readLine();
						System.out.println("[Client] From server: " + mensajeEncriptado);
						Cipher cipher = Cipher.getInstance("RSA");
						cipher.init(Cipher.DECRYPT_MODE, publicKey);
						byte[] encryptedMessageBytess = Base64.getDecoder().decode(mensajeEncriptado);
						byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytess);

						String decryptedMessage = new String(decryptedMessageBytes);
						System.out.println("[Client] decrypted message: " + decryptedMessage);
					}

					ec.init();
					break;
				case 4:
					running = false;
					JOptionPane.showMessageDialog(null, "Saliendo del programa...");
					break;
				default:
					break;
			}
		}











	}


	public void init() throws Exception {
		while (true) {
			clientSideSocket = new Socket(this.server, this.port);

			protocol(clientSideSocket);

			clientSideSocket.close();
		}
	}

	public static void main(String args[]) throws Exception {
		ClienteMensajeria ec = null;
		if (args.length == 0) {
			ec = new ClienteMensajeria();

		} else {
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			ec = new ClienteMensajeria(server, port);
		}
		ec.init();
	}

}
