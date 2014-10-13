import java.io.PrintWriter;
import java.net.Socket;


public class TestSocket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			Socket sock = new Socket("localhost",5555);
			PrintWriter wr = new PrintWriter(sock.getOutputStream());
			for(int i=0;i<10;i++){
				wr.write(i+"\n");
				wr.flush();
				System.out.println("Client: " + i + " checkError: " + wr.checkError());
			}
			sock.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
