import java.net.*;
import java.io.*;
import java.util.*;

class ServeurTempsObjet {
/* Serveur pour les clients ClientTempsObjet */

	static final short port = 1951;

	public static void main(String[] arg) {
		try {
			/* Création de la socket de serveur */
			ServerSocket sock = new ServerSocket(port);
			Socket sockC;	// Socket client

			/* Connexion des clients */
			System.out.println("Serveur disponible, sur le port : " + port);
			while ((sockC = sock.accept()) != null) {
				process(sockC);
			}
		} catch(java.io.IOException e) {
			System.err.println("Erreur du serveur : " + e);
			return;
		}
	}

	static void process(Socket sk) throws java.io.IOException {
		System.out.println("Connexion du client : " + sk.getInetAddress());

		/* Traitement et clôture de la connexion */
		ObjectOutputStream ecriv = new ObjectOutputStream(sk.getOutputStream());
		ecriv.writeObject(new Date());
		ecriv.close();
		sk.close();
	}

}