import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class TestUDPServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		byte[] buffer = new byte[65000];
		int total = 0;
		long lastTime = System.currentTimeMillis();
		DatagramSocket server = null;
		try {
			server = new DatagramSocket(4444);
			while (true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				server.receive(packet);				
				total ++;

			 if((System.currentTimeMillis() - lastTime) > 10*100){
				 lastTime = System.currentTimeMillis();
				 System.out.println("Average EPS: " + (total/10));
				 total = 0;
			 }

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			server.close();
		}
	}

}
