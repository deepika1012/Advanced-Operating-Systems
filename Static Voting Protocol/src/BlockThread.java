import java.util.Random;
import java.util.Scanner;


public class BlockThread implements Runnable {
	static int i;
	int maxBlock = 10;
	static boolean block = false;
	static Thread receiveThread;
	static int[] count = new int[45];
	static BlockThread localApp = new BlockThread();

	public synchronized void run() {
		Scanner reader;
		while (true) {
//			while(block)
//			{
//				try {
//				    synchronized(localApp){
//						System.out.println("* Thread: Blocked due to Failure");
//				    	localApp.wait();
//						System.out.println("* Thread: Unblocked: " + block + "\n");
//				    }
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					System.out.println("* ********* Thread: Interrupted *********");
//					continue;
//				}
//			}
			reader = new Scanner(System.in);
			System.out.println("* Thread: Enter a number: ");
			int a = reader.nextInt();
			System.out.println("* Thread: Number entered is: " + a);

			for(i = 0; i < 10; i++)
				System.out.print(count[i] + ", ");
			System.out.println();
			if( (maxBlock - 1) <= i)
			{
//				a++;
				break;
			}
//			System.out.print("1");
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		reader.close();
		System.out.println("* Thread Exited");
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		receiveThread = new Thread(new BlockThread());
		receiveThread.start();
		
		for(i = 0; i < 10; i++)
		{
//			try {
//			        Random rn = new Random();
//			        long rand1 = (long) (rn.nextGaussian()*8 + 20) * 1000;
////				    BlockThread.block = true;
//					System.out.println("* Application: " + i + ": System Failed: " + rand1);
//			        receiveThread.suspend();
//					Thread.sleep(4000);
//					receiveThread.resume();
//					System.out.println("* Application: " + i + ": System Recovered: " + rand1);
//
////				    synchronized(localApp)
////				    {
////				    	BlockThread.block = false;
////				    	localApp.notify();
////				    	if(!receiveThread.isAlive()) System.out.println("* Application: Thread Died");
////				    	if(receiveThread.isDaemon()) System.out.println("* Application: Thread is Daemon");
////						System.out.println("* Application: " + i + ": System Recovered: " + rand1+ ", State: " + BlockThread.block);
////				    }
////				    Thread.sleep(200);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			count[i] = i ;
		}
		return;
	}
}
