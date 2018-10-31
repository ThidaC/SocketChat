import java.net.*;
import java.io.*;

/* Cette classe se connecte au service DayTime et lit une ligne de texte */
/* Ce service, s'il est disponible, fournit la date et l'heure courantes */ 
class ClientTemps {

	public static void main(String[] arg) {
		String serveur = arg.length == 1 ? arg[0] : "localhost";
		int numeroPort = 13;

		try {
			Socket sock = new Socket(serveur, numeroPort);
			BufferedReader lect = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String lu = lect.readLine();
			System.out.println("Sur " + serveur + " , nous sommes le " + lu);
			sock.close();
		} catch(java.io.IOException e) {
			System.err.println(e);
		}

	}

}