import java.net.*;

class ServeurSimple {
	static final short port = 80;

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
		/* La communication peut avoir lieu maintenant, jusqu'à... */
		sk.close();
	}

}