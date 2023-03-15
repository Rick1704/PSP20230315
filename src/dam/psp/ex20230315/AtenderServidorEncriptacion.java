package dam.psp.ex20230315;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AtenderServidorEncriptacion implements Runnable {

	private Socket socket;

	public AtenderServidorEncriptacion(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		DataInputStream leer;
		DataOutputStream escribir = null;
		try {
			leer = new DataInputStream(socket.getInputStream());
			escribir = new DataOutputStream(socket.getOutputStream());
			String peticion = leer.readUTF();
			switch (peticion) {
			case "hash":
				comprobarHash(leer, escribir);
				break;
			case "cert":
				comprobar_cert(leer, escribir);
				break;
			case "cifrar":
				String alias  = leer.readUTF();
				String codificacion = leer.readUTF();
				
				break;
			default:
				escribir.writeUTF("ERROR:Se esperaba una petición");
				break;
			}

		} catch(SocketTimeoutException e) {
			try {
				escribir.writeUTF("ERROR:Read timed out");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch(IOException e1) {
			try {
				escribir.writeUTF("ERROR:Read timed out");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			try {
				System.out.println(2);
				escribir.writeUTF("ERROR:Read timed out");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void comprobar_cert(DataInputStream leer, DataOutputStream escribir) throws IOException {
		String alias = leer.readUTF();
		String codificacion = leer.readUTF();
		System.out.println(codificacion);
		System.out.println(codificacion);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] resu = codificacion.getBytes();
			byte[] longitudbytes = md.digest(resu);
			String hash = Base64.getEncoder().encodeToString(longitudbytes);
			escribir.writeUTF("OK:" + hash);
		} catch (NoSuchAlgorithmException e) {
			escribir.writeUTF("ERROR:Se esperaba un algoritmo");
		}
	}

	private void comprobarHash(DataInputStream leer, DataOutputStream escribir)
			throws IOException, NoSuchAlgorithmException {
		try {
			String algoritmo = leer.readUTF();

			System.out.println(algoritmo);

			System.out.println(algoritmo);
			byte[] byteleer;
			try {
				byteleer = leer.readAllBytes();
				MessageDigest md;
				md = MessageDigest.getInstance(algoritmo);
				byte[] longitudbytes = md.digest(byteleer);
				String hash = Base64.getEncoder().encodeToString(longitudbytes);
				escribir.writeUTF("OK:" + hash);
			} catch (IOException e) {
				escribir.writeUTF("ERROR:Se esperaban datos");
			} catch (OutOfMemoryError e) {
				escribir.writeUTF("ERROR:Se esperaban datos");
			}

		} catch (EOFException r) {
			escribir.writeUTF("ERROR:Se esperaba un algoritmo");
		} catch (IOException e) {
			escribir.writeUTF("ERROR:Se esperaba un algoritmo");
		}

	}

}
