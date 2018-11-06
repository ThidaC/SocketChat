import java.net.*;
import java.io.*;
import java.util.*;

class ServeurChat {
	ServerSocket sock;				/* Socket de communication */
	int port = 7777;				/* Port de communication */

	final static String Id = "ChatMC ";		/* Nom de conversation du serveur */
	ArrayList clients;				/* Liste des clients connectés */
	ArrayList<String> clientsList = new ArrayList(); // Liste des utilisateurs

	/* LANCEMENT DU SERVEUR DE DISCUSSION */
	public static void main(String[] arg) {
		/* Serveur en démarrage */
		System.out.println("Serveur Chat en lancement...");
		ServeurChat w = new ServeurChat();
		w.tourner();

		/* Arrêt du serveur */
		System.out.println("Serveur Chat indisponible !");
	}

	/* CONSTRUCTEUR DE SERVEUR DE DISCUSSION */
	ServeurChat() {
		/* Initialisations */
		clients = new ArrayList();
		try {
			sock = new ServerSocket(port);
			System.out.println("Serveur actif - scrutation sur le port : " + port);
		} catch (IOException e) {
			System.err.println("Erreur au lancement du Serveur Chat Java !");
			System.exit(0);
		}
	}

	/* TACHE COURANTE DU SERVEUR */
	public void tourner() {
		try {
			while (true) {
				Socket usag = sock.accept();
				String nomUsag = usag.getInetAddress().getHostName();
				System.out.println("Connexion depuis " + nomUsag);
				GestionChat gc = new GestionChat(usag, nomUsag);
				synchronized(clients) {
					clients.add(gc);
					gc.start();
					gc.send(Id + nomUsag + " : client " + clients.size() + " en ligne");
				}
			}
		} catch(IOException e) {
			System.err.println("Erreur de fonctionnement du Serveur Chat !");
			System.exit(0);
		}
	}

	/* CLASSE INTERNE POUR LA GESTION DE LA CONVERSATION */
	class GestionChat extends Thread {
		Socket sockC;				/* Socket du client */
		BufferedReader lect;		/* Lecteur de données sur la socket */
		PrintWriter ecriv;			/* Ecrivain de données sur la socket */
		String clientIP;			/* Machine du client */
		String clientLogin;			/* Nom de conversation du client */

		GestionChat(Socket sk, String nm) {	/* Constructeur */
			sockC = sk;
			clientIP = nm;
			
			try {
				lect = new BufferedReader(new InputStreamReader(sk.getInputStream()));
				ecriv = new PrintWriter(sk.getOutputStream(), true);
			} catch (IOException e) {
				System.err.println("Erreur d'E/S au niveau du serveur");
			}
		}

		public void run() {			/* Gestion de la conversation */
			String lu;
			try {
				while ((lu = lect.readLine()) != null) {
					switch(lu.charAt(0)) {
						case '/' : 
							// Vérifier que le pseudo est unique
							String pseudo = lu.substring(2);
							if (clientsList.size()>=1) {
								for(int i = 0; i < clientsList.size(); i++) {
									if (pseudo.equals(clientsList.get(i))==true) { // Si le pseudo est déjà utilisé, on ajoute le suffixe "2" au pseudo
										String newPseudo = clientsList.get(i)+"2";
										pseudo = newPseudo;
										if ((i+1)==clientsList.size()) { // On ajoute le pseudo une fois qu'on a fini de parcourir le tableau
											clientsList.add(pseudo);
											clientLogin = pseudo;
											break;
										}
									} 
									else {
										clientsList.add(pseudo);
										clientLogin = pseudo;
										break;
									}
								}
							} else {
								clientsList.add(pseudo);
								clientLogin = pseudo;
							}
							System.out.println(clientsList);
							send("> Bienvenue " + clientLogin + " !"); // Un message de bienvenue est envoyé à tout utilisateur qui se connecte
							broadcast("> " + clientLogin + " s'est connecté"); // Tous les utilisateurs sont avertis de toute arrivée d'un nouvel utilisateur
							break;
						case '.' : 
							send("> Au revoir " + clientLogin + " !"); // Un message d'au revoir est envoyé à tout utilisateur qui se déconnecte
							broadcast("> " + clientLogin + " s'est déconnecté"); // Tous les utilisateurs sont avertis de tout départ d'un utilisateur
							clientsList.remove(clientLogin); // On retire l'utilisateur de la liste utilisateurs
							break;
						case '!' : 
							broadcast(clientLogin + " > " + lu.substring(2)); // Le message est envoyé à tous les utilisateurs connectés
							break;
						//case '?' :
						//case '@' : 
						case '%' : 
							// Afficher les pseudonymes de tous les utilisateurs connectés
							String utilisateurs = String.join(", ", clientsList);
							String res = "> Utilisateurs connectés : " + utilisateurs;
							send(res);
						default : send(clientLogin + " > " + lu);
					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}

		public void send(String mess) {	        /* Envoyer un message au client */
			ecriv.println(mess);
		}

		public void broadcast(String mess) {	/* Envoyer un message à tous les clients */
			synchronized(clients) {
				for (int i = 0; i < clients.size(); i++) {
					GestionChat gct = (GestionChat) clients.get(i);
					if (gct != null) gct.send(mess);
				}
			}
		}

		public void close() {			/* Fermer le gestionnaire */
			if (sockC == null) return;
			try {
				sockC.close();
				sockC = null;
			} catch (IOException e) {
				System.err.println("Erreur de fermeture de connexion avec " + clientIP);
			}
		}
	}

}