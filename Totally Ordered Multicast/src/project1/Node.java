
/**
 * @author deepika lakshmanan
 * @package project1
 */

package project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Node implements Serializable {

	public static final Object lock = new Object();
	public static int logicalClock = 1; // Lamport's Logical clock
	int maxSend;
	int sentCount = 0;
	int[][] recv = new int[50][50];
	int[] countMemb = new int[50];
	String[] msg_id = new String[50];
	public static HashMap<Integer, String> nodes = new HashMap<Integer, String>();
	public static int no_of_messages;
	public static int ID;

	public Node() {
	}

	// Read the configuration file: All the nodes information
	public void loadAddress() throws Exception {
		FileInputStream fstream = new FileInputStream("info.text");
		DataInputStream in = new DataInputStream(fstream);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				if (strLine.indexOf("#") > 0) {
					strLine = strLine.substring(0, strLine.indexOf("#")).trim();
					int nodeNo = Integer.parseInt(strLine.substring(0,
							strLine.indexOf(" ")));
					String hostname = strLine.substring(
							strLine.indexOf(" ") + 1, strLine.indexOf("-"));
					System.out.println(strLine + "\n" + nodeNo + "\n"
							+ hostname);
					nodes.put(nodeNo, hostname);
					for (int key : nodes.keySet()) {
						System.out.println(key + ":" + nodes.get(key));
					}
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Read the input file which consists of (SenderId, Receiver1
	// Receiver2,Message)
	public void readinputfile() throws NumberFormatException, IOException {
		int i = 0;
		int j = 0;
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("input.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine = null;
			maxSend = 0;
			while ((strLine = br.readLine()) != null) {
				int senderId;
				String id = Integer.toString(Node.ID);
				String specString = strLine.substring(0, strLine.length());
				String[] specStringLine = specString.split(",");
				senderId = Integer.parseInt(specStringLine[0]);
				if (senderId == ID) {
					msg_id[maxSend] = specStringLine[2];
					String[] specStringLine1 = specStringLine[1].split(" ");
					countMemb[maxSend] = 0;
					for (i = 0; i < specStringLine1.length; i++) {
						recv[maxSend][i] = Integer.parseInt(specStringLine1[i]);
						countMemb[maxSend]++;
					}
					System.out.print("* " + maxSend + " Message: "
							+ msg_id[maxSend] + ", McMemb: "
							+ countMemb[maxSend] + ": ");
					maxSend++;
				}
			}
			int ii, jj;
			for (ii = 0; ii < maxSend; ii++) {
				for (jj = 0; jj < countMemb[ii]; jj++) {
					System.out.print(recv[ii][jj] + " ");
				}
				System.out.println();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		no_of_messages = j - 1;
	}

	public static void main(String[] args) throws Exception {

		Node n1 = new Node();
		Node.ID = Integer.parseInt(args[0]);
		int i;
		n1.loadAddress();
		// Setup tcp connections
		Server S = new Server(n1);
		S.start();
		n1.readinputfile();
		while (true) {
			System.out.println("*******Enter Input******* \n"
					+ "***Enter 1 to call M-Send**** \n"
					+ "***Enter 2 to call M-Receive**** \n"
					+ "***Enter exit to finish****");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String reader = br.readLine();
			if (reader.equals("1")) {
				if (-1 == n1.MSend())
					System.out
							.println("********Maximum Send Limit Reached ******************");
			} else if (reader.equals("2")) {
				if (-1 == n1.MReceive(n1))
					System.out
							.println("********Maximum Receive Limit Reached ******************");
			} else if (reader.equalsIgnoreCase("exit")) {
				break;
			}
		}
	}
//Signal to send the message to members given via input file
	public int MSend() {
		int count = 0;
		int i = 0;
		try {
			System.out.println("Count compare " + sentCount + " " + maxSend);
			if (sentCount > maxSend - 1) {
				System.out.println("Max send limit exceeded");
				return -1;
			}
			Sender(nodes);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i++;
		return 0;
	}
//Signal to start receiving messages 
	public int MReceive(Node l1) {
		try {
			if (sentCount > countMemb.length - 1) {
				System.out.println("Max recv limit exceeded");
				return -1;
			}
			ServerThread.Deliver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
//Skeens Algorithm: Sender's End 
	public void Sender(HashMap<Integer, String> nodes)
			throws ClassNotFoundException {
		PrintWriter out[] = new PrintWriter[10];
		BufferedReader in[] = new BufferedReader[10];
		ArrayList<Integer> recv_ts = new ArrayList<Integer>();
		Socket sock[] = new Socket[50];
		String status1 = "Deliverable";
		String status2 = "UnDeliverable";
		ServerSocket[] server = new ServerSocket[50];
		Socket client[] = new Socket[50];
		Node node;
		int i = 0;
		String hostname = "";
		int j = 0;
		int key;
		int ii = 0;
		int k = 0;
		int jj = 0;
		try {
			synchronized (lock) {
				logicalClock++;
			}
			for (j = 0; j < countMemb[sentCount]; j++) {
				key = recv[sentCount][j];
				hostname = nodes.get(key);
				System.out.println("* Get hostname");
				sock[ii] = new Socket(hostname, 6679);
				System.out.println("* Connected to:" + hostname);
				out[ii] = new PrintWriter(sock[ii].getOutputStream(), true);
				in[ii] = new BufferedReader(new InputStreamReader(
						sock[ii].getInputStream()));
				System.out
						.println("***********Initial timestamp**************");
				String senderId = Integer.toString(ID);
				String msgId = msg_id[sentCount];
				String Status = "U";
				String org_ts = null;
				synchronized (lock) {
					org_ts = Integer.toString(logicalClock);
				}
				String proposed_ts = null;
				String type = "Initial";
				String delim = "-";
				String initialTimestamp = senderId + delim + msgId + delim
						+ Status + delim + org_ts + delim + type + delim
						+ proposed_ts;
				System.out.println("----init msg---" + initialTimestamp);
				out[ii].println(initialTimestamp);
				ii++;
			}
			while (true) {
				for (j = 0; j < countMemb[sentCount]; j++) {
					String fromServer;
					fromServer = in[jj].readLine();
					// *********** Receive the proposed time
					// stamp*****************
					System.out.println("Receiver sent - " + fromServer);
					System.out.println("**Proposed Timestamp of" + j + "----"
							+ fromServer);
					String[] tokens = fromServer.split("-");
					int prop_ts = Integer.parseInt(tokens[5]);
					recv_ts.add((prop_ts));
					synchronized (lock) {
						logicalClock = Math.max(logicalClock,
								Integer.parseInt(tokens[3]));
						logicalClock++;
					}
					jj++;
				}
				if (recv_ts.size() == countMemb[sentCount])
					break;
			}
			Collections.sort(recv_ts, Collections.reverseOrder());
			for (i = 0; i < recv_ts.size(); i++) {
				System.out.println((recv_ts.get(i)).toString());
			}
			String max_prop_ts = Integer.toString(recv_ts.get(0));
			System.out.println("max " + max_prop_ts);
			jj = 0;
			synchronized (lock) {
				logicalClock++;
			}
			for (j = 0; j < countMemb[sentCount]; j++) {
				String senderId = Integer.toString(ID);
				String msgId = msg_id[sentCount];
				String Status = "D";
				String org_ts = null;
				synchronized (lock) {
					org_ts = Integer.toString(logicalClock);
				}
				String final_ts = max_prop_ts;
				String type = "Final";
				String delim = "-";
				String finalTimestamp = senderId + delim + msgId + delim
						+ Status + delim + org_ts + delim + type + delim
						+ final_ts;
				System.out.println("----final msg---" + finalTimestamp);
				out[jj].println(finalTimestamp);
				jj++;
			}
			for (i = 0; i < countMemb[sentCount]; i++) {
				sock[i].close();
			}
			sentCount++;
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: xxx");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
