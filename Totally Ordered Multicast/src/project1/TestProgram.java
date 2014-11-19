
/**
 * @author deepika lakshmanan
 * @package project1
 */

package project1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//1.Open all the log files
//2.Pair wise comparison between all pairs of messages
public class TestProgram {

	public static void main(String[] args) throws IOException {
		int node[] = new int[45];
		if (args.length > 0) {
			int No_of_nodes = Integer.parseInt(args[0]);
			for (int i = 0; i < No_of_nodes; i++) {
				node[i] = Integer.parseInt(args[i + 1]);
			}
			String tokens[][] = new String[No_of_nodes][];
			for (int i = 0; i < No_of_nodes; i++) {
				FileInputStream fstream = new FileInputStream("File" + node[i]
						+ ".txt");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				tokens[i] = br.readLine().split(" ");
			}
			int index1 = 0;
			int index2 = 0;
			for (int p = 0; p < No_of_nodes; p++) {
				for (int i = 0; i < tokens[i].length; i++) {
					for (int j = i + 1; j < tokens[i].length; j++) {
						for (int k = 0; k < No_of_nodes; k++) {
							if (k == i)
								continue;
							System.out.print("i** " + i + " j** " + j + " k** "
									+ k);
							index1 = Arrays.asList(tokens[k]).indexOf(
									tokens[p][i]);
							index2 = Arrays.asList(tokens[k]).indexOf(
									tokens[p][j]);
							System.out.print(" -> tokens[0][i]  :  "
									+ tokens[p][i]);
							System.out.println(",  tokens[0][j]  :  "
									+ tokens[p][j]);
							if (-1 == index1 || -1 == index2)
								continue;
							if (index1 > index2) {
								System.out.println("condition violated");
							}
						}
						System.out.println();
					}
				}
			}
		}
	}
}
