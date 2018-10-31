import java.net.*;

class ClientGeneral {

	public static void main(String[] arg) {
		String serveur = "localhost";
		int numeroPort = 8080;

		try {
			if (arg.length == 2) {
				serveur = arg[0];
				numeroPort = Integer.parseInt(arg[1]);
			} else if (arg.length == 1) serveur = arg[0];
			Socket sock = new Socket(serveur, numeroPort);
			InetAddress distant = sock.getInetAddress();
			System.out.println("Connexion faite sur " + distant.getHostName() + " : " + distant.getHostAddress());
			sock.close();
		} catch (UnknownHostException e) {
			System.err.println("Erreur de nom de serveur : " + serveur + " inconnu");
			System.err.println(e);
			return;			
		} catch (NoRouteToHostException e) {
			System.err.println("Erreur d'acces : " + serveur + " inaccessible");
			System.err.println(e);
			return;			
		} catch (ConnectException e) {
			System.err.println("Erreur d'acces : " + serveur + " refuse la connexion");
			System.err.println(e);
			return;			
		} catch(java.io.IOException e) {
			System.err.println("Erreur de connexion sur " + serveur + " : ");
			System.err.println(e);
			return;
		}

	}

}