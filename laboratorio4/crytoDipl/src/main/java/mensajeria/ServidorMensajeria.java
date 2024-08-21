package mensajeria;

import network.Usuario;
import util.Base64;
import util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServidorMensajeria {
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;

	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;

	private int port;

	private HashMap<String, String> usuarios = new HashMap<String, String>();
	private HashMap<String, ArrayList<String>> buzon = new HashMap<String, ArrayList<String>>();

	public ServidorMensajeria() {
		this.port = PORT;
	}

	public ServidorMensajeria(int port) {
		this.port = port;
	}

	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	private void protocol(Socket socket) throws IOException {
		createStreams(socket);

		String text = fromNetwork.readLine();

		String answer = "";
		String [] array = text.split(" ");

		if(text.contains("REGISTRAR")){

			if(!usuarios.containsKey(array[1])){
				usuarios.put(array[1], array[2]);
				buzon.put(array[1], new ArrayList<>());
				answer = "Bienvenido " + array[1];
			}else
			{
				answer = "El usuario " + array[1] + " ya está registrado";
			}

			toNetwork.println(answer);
			System.out.println("[Server] to client: " + answer);
			System.out.println("[Server] Finished.");
		}

		if(text.contains("OBTENER_LLAVE_PUBLICA")){
			if(usuarios.containsKey(array[1])){
				answer = "Llave pública de " + array[1] + ": " + usuarios.get(array[1]);
			}else
			{
				answer = "ERROR. El usuario  " + array[1] +  " no está registrado" ;
			}

			toNetwork.println(answer);
			System.out.println("[Server] to client: " + answer);
			System.out.println("[Server] Finished.");

		}

		if(text.contains("ENVIAR")){
			if(usuarios.containsKey(array[1])){
				buzon.get(array[1]).add(array[2]);
			}else{
				answer = "ERROR. El usuario  " + array[1] +  " no está registrado" ;
			}

			toNetwork.println(answer);
			System.out.println("[Server] Finished.");
		}

		if(text.contains("LEER")){
			if(usuarios.containsKey(array[1])){
				if(buzon.get(array[1]).size() == 0){
					answer = "El usuario  " + array[1] +  " tiene 0 mensajes" ;
					toNetwork.println(answer);

				}else {
					if(buzon.get(array[1]).size() == 1){
						answer = "El usuario tiene 1 mensaje.";
						toNetwork.println(answer);
					}else {
						answer = "El usuario tiene " + buzon.get(array[1]).size() + "mensajes.";
						toNetwork.println(answer);
					}

					toNetwork.println(buzon.get(array[1]).size());


					for (String cadena: buzon.get(array[1])) {
						System.out.println("DESDE EL SERVER: " + cadena);
						toNetwork.println(cadena);
					}
				}
			}else{
				answer = "ERROR. El usuario  " + array[1] +  " no está registrado" ;
				toNetwork.println(answer);
			}
		}
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
		ServidorMensajeria es = null;
		if (args.length == 0) {
			es = new ServidorMensajeria();
		} else {
			int port = Integer.parseInt(args[0]);
			es = new ServidorMensajeria(port);
		}
		es.init();
	}
}
