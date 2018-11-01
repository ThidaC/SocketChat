import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ClientChat extends Applet {
	Socket sock;					/* Socket de communication */
	static int port = 7777;				/* Port de communication */
	boolean connecte;				/* Etat du client */

	BufferedReader lect;				/* Lecteur de données sur la socket */
	PrintWriter ecriv;				/* Ecrivain de données sur la socket */

	String message;					/* Message écran */
	int debMess = 10;				/* Colonne de début d'écriture du message écran */

	Frame cad;					/* Cadre de discussion */
	final static String titre = "CHAT BOX";		/* Titre dans la barre de titre du salon */

	TextField text;					/* Zone de saisie de texte */
	int dim = 40;					/* Taille de la zone de saisie */
	TextArea conv;					/* Zone d'affichage des conversations */
	final static String pol = "Monospaced";		/* Police de caractère de conversation */
	int tail = 11;					/* Taille des caractères de conversation */

	Button lib;					/* Bouton d'ouverture de session */
	Button lob;					/* Bouton de fermeture de session */
	Panel p = new Panel();				/* Panneau support des boutons de session */
	
	String utilisateur;

	/* DEFINITION DU CADRE DU FORUM */
	public void init() {
		// Bienvenue à l'utilisateur
		message = "Veuillez patienter pendant la mise en place du cadre de discussion...";
		repaint();

		// Mise en place du cadre de discussion
		cad = new Frame(titre);
		cad.setLayout(new BorderLayout());

		// Mise en place de la zone de conversation
		conv = new TextArea();
		conv.setEditable(false);	// zone en lecture seulement
		conv.setFont(new Font(pol, Font.PLAIN, tail));
		cad.add(BorderLayout.NORTH, conv);

		// Mise en place du bouton d'ouverture de session
		p.add(lib = new Button("Entrer"));
		lib.setEnabled(true);
		lib.requestFocus();
		lib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
				lib.setEnabled(false);
				lob.setEnabled(true);
				text.requestFocus();
			}
		});

		// Mise en place du bouton de fermeture de session
		p.add(lob = new Button("Quitter"));
		lob.setEnabled(false);
		lob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout();
				lib.setEnabled(true);
				lob.setEnabled(false);
				lib.requestFocus();
			}
		});

		// Saisie du texte de l'utilisateur et envoi
		p.add(new Label("Votre message :"));
		text = new TextField(dim);
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connecte) {
					ecriv.println(text.getText());
					text.setText("");
				}
			}
		});
		p.add(text);

		// Ajout du panneau au cadre de discussion
		cad.add(BorderLayout.SOUTH,p);

		// Fermeture propre du cadre de discussion
		cad.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// la fermeture est effective après setVisible et dispose
				ClientChat.this.cad.setVisible(false);
				ClientChat.this.cad.dispose();
				logout();
			}
		});
		cad.pack();

		// Centrage du cadre de discussion dans l'écran
		Dimension tailEcr = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension tailCad = cad.getSize();
		int X = (tailEcr.width - tailCad.width)/2;
		int Y = (tailEcr.height - tailCad.height)/2;
		cad.setLocation(X,Y);
		cad.setVisible(true);
		message = "Le cadre de discussion est en place !";
		repaint();
	}

	/* ENTREE DANS LE FORUM */
	public void login() {
		if (connecte) return;
		try {
			/* Ouverture de la connexion */
			sock = new Socket("localhost", port);
			showStatus("CONNEXION ETABLIE !!!");

			/* Création des flux d'entrée et de sortie */
			lect = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			ecriv = new PrintWriter(sock.getOutputStream(), true);
		} catch(IOException e) {
			System.err.println(e);
		}

		/* Annonce au serveur de la connexion d'un nouvel utilisateur */
		String pseudo = text.getText();
		if (pseudo.length() == 0) { //si un utilisateur se connecte sans pseudonyme, il se voit attribuer le pseudonyme "anonyme" par défaut
			ecriv.println("/ Anonyme");
		} else {
			ecriv.println("/ "+pseudo);
		}
		connecte = true;

		/* Construction et démarrage du lecteur */
		/* (sous forme de Thread, pour éviter l'inter blocage avec l'écriture)	*/
		new Thread(new Runnable() {
			public void run() {
				String lu;
				try {
					while (connecte && ((lu = lect.readLine()) != null)) {
						conv.append(lu + "\n");
					}
				} catch(IOException e) {
					showStatus("CONNEXION PERDUE !!!");
					return;
				}
			}
		}).start();
	}

	/* SORTIE DU FORUM */
	public void logout() {
		if (!connecte) return;
		ecriv.println(".");
		connecte = false;
		try {
			if (sock != null) sock.close();
		} catch(IOException e) {
			System.err.println("BIZARRE !!!");
		}
	}

	/* ECRITURE DES MESSAGES A L'ECRAN */
	public void paint(Graphics g) {
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(Color.black);
		g.drawString(message, debMess, getSize().height/2);
	}

}