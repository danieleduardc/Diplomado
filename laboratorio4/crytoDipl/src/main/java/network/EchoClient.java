package network;

import util.Base64;
import util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	public static final String SERVER = "localhost";
	public static final int PORT = 3400;

	private static final Scanner SCANNER = new Scanner(System.in);

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;

	private Socket clientSideSocket;

	private String server;
	private int port;

	public EchoClient() {
		this.server = SERVER;
		this.port = PORT;
	}

	public EchoClient(String server, int port) {
		this.server = server;
		this.port = port;
	}


	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void protocol(Socket socket) throws Exception {
		createStreams(socket);

		Usuario usuario = new Usuario("Daniel Puerta" , 10000);

		byte[] userByte = Util.objectToByteArray(usuario);
		String userB64 = Base64.encode(userByte);

		toNetwork.println(userB64);

		System.out.println("[Client] to server: " + userB64);


		String fromServer = fromNetwork.readLine();
		System.out.println("[Client] From server: " + fromServer);

		System.out.println("[Client] Finished.");
	}

	public void init() throws Exception {
		clientSideSocket = new Socket(this.server, this.port);

		protocol(clientSideSocket);

		clientSideSocket.close();
	}

	public static void main(String args[]) throws Exception {
		EchoClient ec = null;
		if (args.length == 0) {
			ec = new EchoClient();

		} else {
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			ec = new EchoClient(server, port);
		}
		ec.init();
	}
}
