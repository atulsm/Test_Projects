import java.net.InetAddress;

public class TestHostNameResolution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InetAddress inetAdd = InetAddress.getByName("blr-l-SAtul");
			System.out.println(inetAdd.getHostName());
			System.out.println(inetAdd.getCanonicalHostName());
			System.out.println(InetAddress.getLocalHost().getHostName());
			System.out.println(InetAddress.getLocalHost().getCanonicalHostName());

/*
			System.out.println(ConnectorUtil.getHostName("164.99.135.2"));
			System.out.println(ConnectorUtil.getHostName("linux-hmahantesh"));
			System.out.println(ConnectorUtil.getHostName("linux-hmahantesh.blr.novell.com"));
			System.out.println(ConnectorUtil.getHostName("164.99.135.12"));
*/


			
			for (int i = 0; i < 100; i++) {
				// later
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
