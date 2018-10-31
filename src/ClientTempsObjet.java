import java.net.*;
import java.io.*;
import java.util.*;

class ClientTempsObjet {

	public static void main(String[] arg) {
		String serveur = arg.length == 1 ? arg[0] : "localhost";
		int numeroPort = 1951;

		try {
			Socket sock = new Socket(serveur, numeroPort);

			/* Création d'un flux à tampon (pour pouvoir transmettre des objets de grande taille) */
			ObjectInputStream lect = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));

			/* Lecture et validation de l'objet lu */
			Object lu = lect.readObject();
			if (!(lu instanceof Date)) throw new IllegalArgumentException("Date attendue, " + lu + " obtenu");

			/* Transtypage de l'objet en Date et affichage */
			Date d = (Date) lu;
			System.out.println("Sur " + serveur + " , nous sommes le " + d.toString());
			sock.close();
		} catch(ClassNotFoundException e) {
			System.err.println("Date attendue, Classe invalide(" + e + ") obtenue");
		} catch(java.io.IOException e) {
			System.err.println(e);
		}

	}

}