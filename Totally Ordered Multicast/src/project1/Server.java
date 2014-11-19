
/**
 * @author deepika lakshmanan
 * @package project1
 */

package project1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import project1.Node.*;

public class Server extends Thread {
	ServerSocket server;
	private final static int port = 6679;
	Socket client;
	Node nd;

	Server(Node node) {

		this.nd = node;
	}

	synchronized public void run() {
		System.out.println("Hello");
		int i = 0;
		String s;
		boolean flag = true;
		try {
			server = new ServerSocket(port);
			while (true) {
				client = server.accept();
				ServerThread st = new ServerThread(client, nd);
				st.start();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}