package zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {
	ServerSocketChannel listener = null;

	protected void mySetup() {
		InetSocketAddress listenAddr = new InetSocketAddress(9026);

		try {
			listener = ServerSocketChannel.open();
			ServerSocket ss = listener.socket();
			ss.setReuseAddress(true);
			ss.bind(listenAddr);
			System.out.println("Listening on port : " + listenAddr.toString());
		} catch (IOException e) {
			System.out.println("Failed to bind, is port : " + listenAddr.toString() + " already in use ? Error Msg : "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		TransferToServer dns = new TransferToServer();
		dns.mySetup();
		dns.readData();
	}

	private void readData() {
		ByteBuffer buf = ByteBuffer.allocate(4096);
		byte[] byteArray = new byte[4096];
		try {
			while (true) {
				SocketChannel conn = listener.accept();
				System.out.println("Accepted : " + conn);
				conn.configureBlocking(true);
				int nread = 0;
				while (nread != -1) {
					try {
						nread = conn.read(buf);
					} catch (IOException e) {
						e.printStackTrace();
						nread = -1;
					}
					
					buf.flip();
					while(buf.hasRemaining()){
					      System.out.print((char) buf.get()); // read 1 byte at a time
					  }
					buf.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
