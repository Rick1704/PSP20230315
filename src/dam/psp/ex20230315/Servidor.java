package dam.psp.ex20230315;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

	public static void main(String[] args) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(50);
		try (ServerSocket serverSocket = new ServerSocket(9000)) {
			System.out.println("Servidor de contactos escuchando en el puerto 9000");
			while (true) {
				Socket socket = serverSocket.accept();
				executor.execute(new AtenderServidorEncriptacion(socket));
			}
		}

	}

}
