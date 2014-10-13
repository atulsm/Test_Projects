import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import sun.net.www.http.HttpClient;

public class ClientSetup {

	public static void main(String[] args) throws Exception {
		HttpClient client1  = new HttpClient(new URL("http://www.swic.edu"),"", 80);
		//client.getOutputStream()
		
		//Socket client1 = new Socket("www.swic.edu", 80);
		
	//	PrintWriter pout = new PrintWriter(out, true);
	//	pout.println("Connected");

		InputStream in = client1.getInputStream();
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String response = bin.readLine();

		System.out.println(response);
		
		//ObjectOutputStream oout = new ObjectOutputStream(out);
		//oout.writeObject(new java.util.Date());
		//oout.flush();

		//client1.close();
	}
}
