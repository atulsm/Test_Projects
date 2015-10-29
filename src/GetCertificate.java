import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import esecurity.base.security.Base64;

public class GetCertificate {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		GetCertificate test = new GetCertificate();
		System.out.println(test.getPublicCertificate("164.99.175.156", 8443));
	}

	public String getPublicCertificate(String host, int port) throws Exception {
		SSLContext context = SSLContext.getInstance("SSL");

		SavingTrustManager tm = new SavingTrustManager();
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();

		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
			System.out.println("Starting SSL handshake...");
			socket.startHandshake();
			socket.close();
		} catch (SSLException e) {
			e.printStackTrace();
		}

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
			System.out.println("Could not obtain server certificate chain");
			return null;
		}

		return derToPEM(chain[0]);
	}

	final class SavingTrustManager implements X509TrustManager {
		private X509Certificate[] chain;

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.chain = chain;
		}
	}

	private static final String PEM_HEADER = "-----BEGIN CERTIFICATE-----\n";
	private static final String PEM_TRAILER = "\n-----END CERTIFICATE-----";

	public static String derToPEM(X509Certificate cert) throws CertificateEncodingException {
		if (cert == null) {
			throw new IllegalArgumentException("No certificate found");
		}
		String b64 = new Base64().encode(cert.getEncoded());
		StringBuilder sb = new StringBuilder();
		sb.append(PEM_HEADER).append(b64).append(PEM_TRAILER);
		return sb.toString();
	}

}
