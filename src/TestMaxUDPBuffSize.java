import java.net.DatagramSocket;


public class TestMaxUDPBuffSize {

	public static int MAX_MSG_SIZE;
    static{
    	try{
    		MAX_MSG_SIZE = new DatagramSocket().getSendBufferSize();
    	}catch (Exception e) {
    		MAX_MSG_SIZE = 1400;
		}
    }
	public static void main(String[] args) {
		System.out.println(MAX_MSG_SIZE);
	}

}
