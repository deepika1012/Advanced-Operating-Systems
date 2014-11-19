import java.io.*;

public class TestingProg {
	public static void main(String args[]) {
		int result = 0;
		int MaxFiles = Integer.parseInt(args[1]);
		int totalNodes = Integer.parseInt(args[0]);

		System.out
				.println("* -------------------------------------------------");
		System.out.println("* Testing Write and Read Operations Consistency");
		System.out
				.println("* -------------------------------------------------");

		for (int i = 1; i <= MaxFiles; i++) {

			String fname = "Log" + i;

			try {
				FileInputStream fstream = new FileInputStream(fname);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					String tokens[] = line.split(",");
					String tokens1[] = tokens[1].split("=");
					String tokens2[] = tokens[2].split("=");
					String s1 = tokens1[1].substring(1, 2);
					String s2 = tokens2[1].substring(1, 2);
					int writeVal = Integer.parseInt(s1);
					int readVal = Integer.parseInt(s2);
					if (readVal >= 1 && writeVal >= 1) {
						result = 1;
						break;
					} else if (writeVal > 1) {
						result = 2;
						break;
					} else {
						result = 0;
					}
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result == 1) {
				System.out.println("* Log" + i
						+ ": ********** Read and write opearations were concurrent :-( ********** ");
			} else if (result == 2) {
				System.out.println("* Log" + i
						+ ": ********** Write opearations were concurrent :-( ********** ");
			} else if (result == 0) {
				System.out.println("* Log" + i
						+ ": Replica Consistency Maintained :-)");
			}
		}

		int j;
		String tokens[] = null;
		String tokens1[] = null;
		String fname = null;
		int[] verNumList = new int[50];
		String[] data = new String[50];

		result = 0;
		int result1 = 0;
		System.out
				.println("* -------------------------------------------------");
		System.out.println("* Testing Version Number and Data  Consistency");
		System.out
				.println("* -------------------------------------------------");
		for (int i = 0; i < MaxFiles; i++) {
			for (j = 1; j <= totalNodes; j++)
			{
				fname = "file" + (i + 1) + j;
				try {
					FileInputStream fstream = new FileInputStream(fname);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String line;
					if ((line = br.readLine()) != null)
						tokens = line.split(":");

					if ((line = br.readLine()) != null)
						tokens1 = line.split(":");

					verNumList[j - 1] = Integer.parseInt(tokens[1]);
					data[j - 1] = tokens1[1];

					br.close();
				} catch (Exception e) {
					// e.printStackTrace();
					 System.out.println("* File " + fname + " not Found");
					 result1 = 1;
				}
			}
			if(0 == result1)
			{
				for (int k = 0; k < verNumList.length; k++)
					for (int k1 = 0; k1 < verNumList.length; k1++) {
						if (k == k1)
							continue;
						if (0 == verNumList[k])
							break;
						if (verNumList[k] == verNumList[k1]) {
							if (data[k].equals(data[k1]))
								continue;
							else {
								result = 1;
								System.out
										.println("* ********** Inconsistency ********** file" + (i+1) + (k+1) + "("
												+ verNumList[k]
												+ " -> "
												+ data[k]
												+ "), file" + (i+1) + (k1+1) + "("
												+ verNumList[k1]
												+ " -> "
												+ data[k1] + ")");
							}
						}
					}
	
				if (0 == result) {
					System.out.println("* file" + (i + 1)
							+ ": Version Numbers and Data are Consistent");
				}
			}
		}
		System.out
				.println("* -------------------------------------------------");
	}
}
