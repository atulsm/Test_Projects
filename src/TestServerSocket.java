import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class TestServerSocket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			ServerSocket serv = new ServerSocket(1468);
			Socket client = serv.accept();
			//client accepts input, do nothing and close on the 5th input
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			int i = 0;
			String in = br.readLine();
			while(in != null){
				if(i++ ==50){
					break;
				}
				in = br.readLine();
				System.out.println("Server: " + in);
			}
			System.out.println("Server exiting");
			client.close();
			serv.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
