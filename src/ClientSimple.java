import java.net.*;

class ClientSimple {

	public static void main(String[] arg) {
		String serveur = "localhost";

		try {
			/* Cr�ation de la socket sur le serveur localhost, via le port 80 */
			Socket sock = new Socket(serveur, 80);

			// C'est le constructeur de java.net.Socket qui effectue les appels
			// � getHostbyname(), � socket(), et � connect(), pour respectivement
			// trouver le serveur � partir de son nom,
			// cr�er le lien et configurer la structure sockaddr_in du serveur,
			// et �tablir la connexion

			/* Maintenant les entr�es/sorties sur la socket sont possibles */
			System.out.println(" *** Connexion accomplie sur " + serveur + " ***");

			// ...faire les entr�es/sorties ici

			/* Fermeture de la socket */
			sock.close();

		} catch(java.io.IOException e) {
			System.err.println("Erreur de connexion sur " + serveur + " : " + e);
			return;
		}
	}

}