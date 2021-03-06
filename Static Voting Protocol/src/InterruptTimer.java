import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import javax.swing.Timer;

import com.sun.nio.sctp.SctpChannel;

public class InterruptTimer extends Thread implements ActionListener {

	/**
	 * @param args
	 */
	Timer timer;
	public SctpChannel localSock;
	
	InterruptTimer() {
		timer = new Timer(2000, this);
		timer.start();
	}
	void interruptHandler(SctpChannel sc)
	{
		this.localSock = sc;
	}

	public static void main(String[] args) {

		while (true) 
		{
			InetSocketAddress serverAddr;
			ByteBuffer buffer = ByteBuffer.allocateDirect(60);

			serverAddr = new InetSocketAddress("10.176.67.65",  15015);
			SctpChannel sc = null;
			InterruptTimer local = new InterruptTimer();
			while(true)
			{
				try {
					System.out.println("Waiting On Receive");
					sc = SctpChannel.open(serverAddr, 0, 0);
					local.localSock = sc;
					sc.receive(buffer, null, null);
					System.out.println("Received");
					buffer.flip();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("* Timer Expired");
		try {
			if(null != this.localSock) {
				if(this.localSock.isOpen()) {
					this.localSock.close(); 
					System.out.println("Closing Sock: " + localSock);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Errorr");
		}
	}
}
