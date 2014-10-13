import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class TestUDPClient {

	public static void main(String[] args) throws Exception {
		System.out.println(new DatagramSocket().getSendBufferSize());
		DatagramSocket udpSocket = new DatagramSocket();
		int bufSize = udpSocket.getSendBufferSize();
		byte[] buf = "Test Data1".getBytes();
		DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("164.99.136.103"), 514);
		udpSocket.send(udpPacket);
	}

}
