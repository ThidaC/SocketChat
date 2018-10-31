import java.net.*;
import java.io.*;

/* **************************************************	*/
/* Thread de gestion d'une extrémité de communication	*/
/* **************************************************	*/
class Pipe extends Thread {
	DataInputStream lect;
	PrintStream ecriv;

	// Constructeur :
	// se brancher sur une entrée et sur une sortie
	Pipe(InputStream fluxEntree, OutputStream fluxSortie) {
		lect = new DataInputStream(fluxEntree);
		ecriv = new PrintStream(fluxSortie);
	}

	// Méthode principale du Thread :
	// lecture d'une ligne sur le flux d'entrée et écriture sur le flux de sortie
	public void run() {
		String lu;
		try {
			while((lu = lect.readLine()) != null) {
				ecriv.print(lu);
				ecriv.print("\r\n");
				ecriv.flush();
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}

/* ************************************	*/
/* Client Telnet minimal (pas d'option)	*/
/* ************************************	*/
class ClientTelnet {

	public static void main(String[] arg) {
		String serveur = arg.length >= 1 ? arg[0] : "localhost";
		int numeroPort = arg.length >=2 ? Integer.parseInt(arg[1]) : 23;

		try {
			Socket sock = new Socket(serveur, numeroPort);
			System.out.println("Connexion accomplie sur " + serveur + " : " + numeroPort);

			/* Création et démarrage d'un thread qui oriente la sortie du client vers l'entrée du serveur */
			new Pipe(sock.getInputStream(), System.out).start();

			/* Création et démarrage d'un thread qui oriente la sortie du serveur vers l'entrée du client */
			new Pipe(System.in, sock.getOutputStream()).start();

		} catch(java.io.IOException e) {
			System.err.println(e);
			return;
		}
	}

}