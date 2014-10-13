import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestURL {

	public static void main(String[] args) {
		try {
			String https_url = "https://164.99.86.70:8443";
			URL url;
			try {

				url = new URL(https_url);
				HttpsURLConnection sconn = (HttpsURLConnection) url.openConnection();
				
				SSLContext sslContext = SSLContext.getInstance("SSLv3");
				sslContext.init(null,trustAllCerts,new java.security.SecureRandom());
				sconn.setSSLSocketFactory(sslContext.getSocketFactory());
				
				sconn.setHostnameVerifier(new HostnameVerifier(){					
					@Override
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});
	

				print_https_cert(sconn);

				// dump all the content
				print_content(sconn);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            @Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
			public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
            @Override
			public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

	private static void print_https_cert(HttpsURLConnection con) {

		if (con != null) {

			try {

				System.out.println("Response Code : " + con.getResponseCode());
				System.out.println("Cipher Suite : " + con.getCipherSuite());
				System.out.println("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					System.out.println("Cert Type : " + cert.getType());
					System.out.println("Cert Hash Code : " + cert.hashCode());
					System.out.println("Cert Public Key Algorithm : "
							+ cert.getPublicKey().getAlgorithm());
					System.out.println("Cert Public Key Format : "
							+ cert.getPublicKey().getFormat());
					System.out.println("\n");
				}

			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static void print_content(HttpsURLConnection con) {
		if (con != null) {

			try {

				System.out.println("****** Content of the URL ********");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));

				String input;

				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
