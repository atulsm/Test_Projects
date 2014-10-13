import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TestCompressionSocket {

	private static int port = 2222;	
	private static String BATCH_DELIMITER = "<b>";

	public static void main(String[] args) throws Exception {
		new Server().start();
		Thread.currentThread().sleep(2000);
		new Client().start();
	}

	private static class Client extends Thread {
		@Override
		public void run() {
			try {
				Socket sock = new Socket("localhost", port);
				BASE64Encoder encoder = new BASE64Encoder();
				PrintWriter pw = new PrintWriter(sock.getOutputStream());

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream gzOut = new GZIPOutputStream(out);
				//String str =	 "very very big number is incoming. very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.";
				//String str = "big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming. big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.";
				//String str =	 "very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.very very big number is incoming.";
				String str = "Testing Dummy Agent";


				//BASE64Encoder encoder = new BASE64Encoder();
				
				int i = 0;
				while (i++ < 500) {
					gzOut.write(str.getBytes("UTF-8"));
					gzOut.flush();
					gzOut.finish();
					out.flush();

					//pw.write(base64.encodeToString(out.toByteArray()));
					pw.write(encoder.encode(out.toByteArray()));
					pw.write(BATCH_DELIMITER);
					pw.write("\n");
					pw.flush();

					out = new ByteArrayOutputStream();
					gzOut = new GZIPOutputStream(out);
				}

				gzOut.close();
				Thread.currentThread().sleep(2000);
				sock.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static class Server extends Thread {
		@Override
		public void run() {
			try {
				ServerSocket serv = new ServerSocket(port);
				Socket sock = serv.accept();
				BASE64Decoder decoder = new BASE64Decoder();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				String str = readEncodedLine(br); //br.readLine();
				while (str != null) {
					//System.out.println(str);
					// this is compressed data, base 64 encoded
					ByteArrayInputStream in = new ByteArrayInputStream(decoder.decodeBuffer(str));
					GZIPInputStream gzis = new GZIPInputStream(in);
					InputStreamReader inStreamReader = new InputStreamReader(
							gzis, "UTF-8");

					/*
					 * Read all the data from the input stream
					 */
					char[] buffer = new char[1024];
					int read = 0;
					StringBuffer ret = new StringBuffer();

					while ((read = inStreamReader.read(buffer)) > 0) {
						ret.append(buffer, 0, read);
					}

					System.out.println(ret.toString());
					str = readEncodedLine(br);
				}
				sock.close();
				serv.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Base64 encoding adds a new line after 76th character.
		 * Check for BATCH_DELIMITER at the end
		 */
		private String readEncodedLine(BufferedReader br) throws IOException{
			StringBuilder b = new StringBuilder();
			String str = null;
			while((str=br.readLine())!= null){				
				if(str.contains(BATCH_DELIMITER)){
					int length = str.length();
					return b.append(str.substring(0, length-BATCH_DELIMITER.length())).toString();
				}else{
					b.append(str);
				}
			}
			return null;
		}
	}

}
