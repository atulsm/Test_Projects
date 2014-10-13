import javax.net.ssl.SSLContext;


public class TestTLSV1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SSLContext context = SSLContext.getInstance("TLSV1.1");
	/*	SSLSocketFactory sf = context.getSocketFactory();
        String[] cipherSuites = sf.getSupportedCipherSuites();
		System.out.println(sf.getDefaultCipherSuites());
		*/
		System.out.println(context.getProtocol());

	}

}
