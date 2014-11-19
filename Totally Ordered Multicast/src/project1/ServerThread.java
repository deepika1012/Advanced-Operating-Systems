/**
 * @author deepika lakshmanan
 * @package project1
 */

package project1;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import project1.Node.*;

//Skeens Algorithm: Receiver's End
public class ServerThread extends Thread {
	public class Message implements Comparable<Message>, Serializable {
		public String message_id;
		public String type;
		public String MsgStatus = null;
		public int max_prop_ts;
		public int prop_ts;
		public int senderId = 0;
		public int org_ts;

		public int getmax_prop_ts() {
			return max_prop_ts;
		}

		public int compareTo(Message final_ts) {
			int compareFinal_ts = ((Message) final_ts).getmax_prop_ts();
			return this.max_prop_ts - compareFinal_ts;
		}
	}

	Message mess1 = new Message();
	Message mess2 = new Message();
	Socket socket;
	BufferedReader br;
	Node node;
	boolean stat = true;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;
	Socket sock[] = new Socket[50];
	public static java.util.List<Message> deliver = Collections
			.synchronizedList(new ArrayList<Message>());

	public ServerThread(Socket client, Node n) {
		this.socket = client;
		this.node = n;
	}

	// create socket connections to send the local timestamp to the sender
	synchronized public void run() {
		boolean flag = false;
		BufferedReader in = null;
		PrintWriter pw = null;
		int ii = 0;
		String hostname;
		try {
			int k = 0;
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String fromSender = in.readLine();
			System.out.println("Reading initial timestamp - " + fromSender);
			String[] tokens = fromSender.split("-");
			synchronized (Node.lock) {
				Node.logicalClock = Math.max(Node.logicalClock,
						Integer.parseInt(tokens[3]));
				Node.logicalClock++;
			}
			pw = new PrintWriter(socket.getOutputStream(), true);
			String senderId = tokens[0];
			String msgId = tokens[1];
			String Status = "P";
			String prop_ts = null;
			synchronized (Node.lock) {
				prop_ts = Integer.toString(Node.logicalClock);
			}
			String type = "Proposed";
			String delim = "-";
			String org_ts = null;
			synchronized (Node.lock) {
				Node.logicalClock++;
				org_ts = Integer.toString((Node.logicalClock));
			}
			// ***********Initialize mess1*******************
			mess1.max_prop_ts = Integer.parseInt(prop_ts);
			mess1.org_ts = Integer.parseInt(tokens[3]);
			mess1.MsgStatus = tokens[2];
			mess1.message_id = tokens[1];
			mess1.senderId = Integer.parseInt(tokens[0]);
			mess1.type = tokens[4];
			mess1.prop_ts = Integer.parseInt(prop_ts);
			synchronized (Node.lock) {
				deliver.add(mess1);
				Collections.sort(deliver);
				for (int i = 0; i < deliver.size(); i++) {
					System.out.println((deliver.get(i).max_prop_ts
							+ "Message: " + deliver.get(i).message_id
							+ "   Message status " + deliver.get(i).MsgStatus));
				}
			}
			// *********************************************
			System.out.println("prop_ts***** " + tokens[0] + tokens[1]
					+ tokens[2] + tokens[3] + tokens[4] + tokens[5]);
			String proposedTimestamp = senderId + delim + msgId + delim
					+ Status + delim + org_ts + delim + type + delim + prop_ts;
			System.out.println("---prop_ts---" + proposedTimestamp);
			System.out
					.println("**************send proposed timestamp************");
			pw.println(proposedTimestamp);
			String final_timestamp = in.readLine();
			System.out.println("****final timestamp***" + final_timestamp);
			tokens = final_timestamp.split("-");
			synchronized (Node.lock) {
				Node.logicalClock = Math.max(Node.logicalClock,
						Integer.parseInt(tokens[3]));
				Node.logicalClock++;
			}
			// ***********Initialize mess2*******************
			mess2.max_prop_ts = Integer.parseInt(tokens[5]);
			mess2.org_ts = Integer.parseInt(tokens[3]);
			mess2.MsgStatus = tokens[2];
			mess2.message_id = tokens[1];
			mess2.senderId = Integer.parseInt(tokens[0]);
			mess2.type = tokens[4];
			mess2.prop_ts = Integer.parseInt(tokens[5]);
			synchronized (Node.lock) {
				deliver.remove(mess1);
				deliver.add(mess2);
				Collections.sort(deliver);
				for (int i = 0; i < deliver.size(); i++) {
					System.out.println((deliver.get(i).max_prop_ts
							+ "Message: " + deliver.get(i).message_id
							+ "   Message status " + deliver.get(i).MsgStatus));
				}
			}
			// *********************************************
			synchronized (deliver) {
			}
			System.out.println(" sent to client \n");
			pw.close();
			in.close();
			ii++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Method to deliver the final msg when MReceive is called.
	public static void Deliver() throws IOException {
		Message deliverMsg = null;
		while (true) {
			if (!deliver.isEmpty()) {
				System.out.println("Queue is not empty");
				while (true) {
					if (deliver.get(0).MsgStatus.contains("D")) {
						writeLog();
						break;
					} else {
						try {
							System.out.println("********no msg to deliver***");
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			} else {
				try {
					System.out
							.println("Queue is  empty**********Sleeping**********");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// write to file
	private static void writeLog() throws IOException {

		System.out.println("Message: " + deliver.get(0).message_id);
		String Message = deliver.get(0).message_id;
		File file = new File("File" + Node.ID + ".txt");

		if (!file.exists()) {
			file.createNewFile();
		}
		System.out.println("file created");
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(Message + " ");
		bw.close();
		fw.close();

		synchronized (Node.lock) {
			deliver.remove(0);
		}
	}
}
