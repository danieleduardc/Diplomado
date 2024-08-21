package network;

import util.Base64;
import util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class EchoServer {
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;

	private int port;

	private HashMap<String, Double> cuentas = new HashMap<String, Double>();

	public EchoServer() {
		this.port = PORT;
	}

	public EchoServer(int port) {
		this.port = port;
	}

	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	private void protocol(Socket socket) throws IOException {
		createStreams(socket);

		String usuarioB64 = fromNetwork.readLine();
		byte[] usuerByte = Base64.decode(usuarioB64);
		Usuario user = null;
		try {
			 user = (Usuario) Util.byteArrayToObject(usuerByte);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		System.out.println("[Server] From client: " + user.toString());
		String answer = "";
		if (cuentas.containsKey(user.getNombre())){
			Double saldoActual = cuentas.get(user.getNombre());
			cuentas.put(user.getNombre(), saldoActual+user.getMonto());
			answer += "Transaccion realizada. Actual Saldo: " + (saldoActual+user.getMonto());
		} else {
			cuentas.put(user.getNombre(), user.getMonto());
			answer += "Cuenta creada exitosamente. Saldo: " + user.getMonto();
		}

		toNetwork.println(answer);
		System.out.println("[Server] to client: " + answer);
		System.out.println("[Server] Finished.");
	}

	private void init() throws IOException {
		listener = new ServerSocket(this.port);

		while (true) {
			serverSideSocket = listener.accept();

			String ip = serverSideSocket.getInetAddress().getHostAddress();
			int port = serverSideSocket.getPort();
			System.out.println("Client IP addres: " + ip);
			System.out.println("Client number port: " + port);

			protocol(serverSideSocket);
		}
	}

	public static void main(String args[]) throws Exception {
		EchoServer es = null;
		if (args.length == 0) {
			es = new EchoServer();
			System.out.println("Server running on port: " + PORT);
		} else {
			int port = Integer.parseInt(args[0]);
			es = new EchoServer(port);
		}
		es.init();
	}
}
