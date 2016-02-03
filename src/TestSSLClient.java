import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TestSSLClient {
	// private static final String SERVER = "localhost";
	private static final String SERVER = "164.99.174.174";
	private static final int PORT = 2620;

	public static void main(String[] args) throws Exception {
		try {
			SSLSocket soc = soc = (SSLSocket)getSSLSocket();
			String[] list = soc.getEnabledCipherSuites();
			System.out.println(Arrays.asList(list));
			
			BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			System.out.println(br.readLine());
			
			for(String cipher : list){
				soc.setEnabledCipherSuites(new String[]{cipher});
				PrintWriter writer = new PrintWriter(soc.getOutputStream());
				writer.write("Test" + "\n");
				writer.flush();

			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Socket getSSLSocket() {
		SSLSocketFactory factory = null;
		try {
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks = null;

			try {
				//ctx = SSLContext.getInstance("TLSv1.2");
				ctx = SSLContext.getInstance("SSLv3");
			} catch (Exception e) {
				ctx = SSLContext.getInstance("TLS");
			}

			kmf = KeyManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
			ks = KeyStore.getInstance("JKS");
			ks.load(null, null);
			kmf.init(ks, null);

			TrustManagerFactory tmFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmFactory.init(ks);
			TrustManager[] trustManagers = tmFactory.getTrustManagers();
			X509TrustManager defaultTM = (X509TrustManager) trustManagers[0];
			X509TrustManager allowAll = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					if (true) {
						return;
					}

					if (chain != null && chain.length > 0) {
						for (X509Certificate cert : chain) {
							cert.checkValidity();
						}
					}
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};

			ctx.init(kmf.getKeyManagers(), new TrustManager[] { allowAll },
					null);

			factory = ctx.getSocketFactory();
			return (SSLSocket) factory.createSocket(SERVER, PORT);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
