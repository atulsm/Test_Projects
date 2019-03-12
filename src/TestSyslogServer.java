import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Demonstrating a syslog server
 * @author SAtul
 *
 */
public class TestSyslogServer {

	private static final int SLEEP_TIME_AFTER_EVENT_MS=10000;

	public static void main(String[] args) {
		try{
			ServerSocket serv = new ServerSocket(1468);
			while(true) {
				Socket client = serv.accept();
				//client accepts input, do nothing and close on the 5th input
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				int i = 0;
				String in;
				while((in = br.readLine()) != null){				
					System.out.println("Server: " + in);
					
					if(SLEEP_TIME_AFTER_EVENT_MS > 0) {
						Thread.sleep(SLEEP_TIME_AFTER_EVENT_MS);
					}
				}			
				System.out.println("Read complete. Listening for new connection.. ");
				client.close();
			}
					
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
