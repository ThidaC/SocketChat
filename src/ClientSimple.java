import java.net.*;

class ClientSimple {

	public static void main(String[] arg) {
		String serveur = "localhost";

		try {
			/* Création de la socket sur le serveur localhost, via le port 80 */
			Socket sock = new Socket(serveur, 80);

			// C'est le constructeur de java.net.Socket qui effectue les appels
			// à getHostbyname(), à socket(), et à connect(), pour respectivement
			// trouver le serveur à partir de son nom,
			// créer le lien et configurer la structure sockaddr_in du serveur,
			// et établir la connexion

			/* Maintenant les entrées/sorties sur la socket sont possibles */
			System.out.println(" *** Connexion accomplie sur " + serveur + " ***");

			// ...faire les entrées/sorties ici

			/* Fermeture de la socket */
			sock.close();

		} catch(java.io.IOException e) {
			System.err.println("Erreur de connexion sur " + serveur + " : " + e);
			return;
		}
	}

}