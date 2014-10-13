import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TestSyslogSSL {

	public static void main(String[] args) throws Exception {
		if(args.length != 2){
			System.out.println("Usage: java TestSyslogSSL IP HOST");
		}
		
		String strHostName = args[0];
		int iPort = Integer.parseInt(args[1]);
		
		SSLSocketFactory factory = null;
		try {
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks;

			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
			ks = KeyStore.getInstance("JKS");
			
			String password = "novell";
			String trustStoreLoc = "D:\\test\\test\\server.jks";
			File trustStoreFile = new File(trustStoreLoc);

			if(false){
				ks.load(new FileInputStream(trustStoreFile), password.toCharArray());
				kmf.init(ks, password.toCharArray());
			}else{
				ks.load(null,null);
				kmf.init(ks, null);
			}

			TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmFactory.init(ks);
			TrustManager[] trustManagers = tmFactory.getTrustManagers();
			X509TrustManager defaultTM = (X509TrustManager) trustManagers[0];
			X509TrustManager allowAll = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

			};
			
			ctx.init(kmf.getKeyManagers(),new TrustManager[]{allowAll},null);

			factory = ctx.getSocketFactory();
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

		SSLSocket m_socket = (SSLSocket)factory.createSocket(strHostName, iPort);
		m_socket.startHandshake();

		PrintWriter m_out = new PrintWriter( m_socket.getOutputStream(), true);
		m_out.write("Jan 16 14:1:30 130.57.171.111 %ASA-5-611103: User logged out: Uname: atul");
		m_out.write("Jan 16 14:2:31 130.57.171.111 %ASA-5-611103: User logged out: Uname: soman");
		
		m_out.close();
		m_socket.close();
	}

}
