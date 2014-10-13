import java.net.InetAddress;

import esecurity.ccs.comp.evtsrcmgt.connector.util.ConnectorUtil;

public class TestHostNameResolution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InetAddress inetAdd = InetAddress.getByName("164.99.87.48");
			System.out.println(inetAdd.getHostName());
			System.out.println(inetAdd.getCanonicalHostName());


			System.out.println(ConnectorUtil.getHostName("164.99.135.2"));
			System.out.println(ConnectorUtil.getHostName("linux-hmahantesh"));
			System.out.println(ConnectorUtil.getHostName("linux-hmahantesh.blr.novell.com"));
			System.out.println(ConnectorUtil.getHostName("164.99.135.12"));



			
			for (int i = 0; i < 100; i++) {
				// later
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
