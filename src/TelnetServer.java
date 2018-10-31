import java.net.*;
import java.io.*;
import java.util.*;

class TelnetServer {
	public static void main(String arg[]) {
		int port = 23;
		try { ServerSocket ssk = new ServerSocket(port);
			Socket skC;
			System.out.println("Server is running on: " + port);
			while ((skC = ssk.accept()) != null) process(skC);
		} catch(java.io.IOException e) {
			System.err.println("Error on server: " + e);
		}
	}

	static void process(Socket sck) throws java.io.IOException {
		System.out.println("Connection of client: " + sck.getInetAddress());
		BufferedReader br = new BufferedReader(new InputStreamReader(sck.getInputStream()));
		PrintWriter pw = new PrintWriter(sck.getOutputStream());
		String st;
		try {
			while((st = br.readLine()) != null) {
				String tdy = (new Date()).toString();
				StringTokenizer tkz = new StringTokenizer(tdy);
				String wday = tkz.nextToken();
				String month = tkz.nextToken();
				String mday = tkz.nextToken();
				String mhours = tkz.nextToken();
				String mtimezone = tkz.nextToken();
				String myear = tkz.nextToken();
				if (st.compareTo("tdy") == 0) st = tdy;
				else if (st.compareTo("ddd") == 0) st = wday;
				else if (st.compareTo("mmm") == 0) st = month;
				else if (st.compareTo("DDD") == 0) st = mday;
				else if (st.compareTo("hhh") == 0) st = mhours;
				else if (st.compareTo("fff") == 0) st = mtimezone;
				else if (st.compareTo("yyy") == 0) st = myear;
				else if (st.compareTo("ext") == 0) st = "Bye";
				else st ="I don't understand!";
				pw.print(st);
				pw.print("\r\n");
				pw.flush();
				if (st.compareTo("Bye") == 0) break;
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		br.close();
		sck.close();
	}
}