import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TestSSLProvider {

	public static void main(String[] args) {
		PrintStream out = System.out;
		out.println("\nTesting socket factory with SSLContext:");
		try {
			// SSLContext protocols: TLS, SSL, SSLv3
			SSLContext sc = SSLContext.getInstance("SSLv3");
			System.out.println("\nSSLContext class: " + sc.getClass());
			System.out.println("   Protocol: " + sc.getProtocol());
			System.out.println("   Provider: " + sc.getProvider());

			// SSLContext algorithms: SunX509
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			System.out.println("\nKeyManagerFactory class: " + kmf.getClass());
			System.out.println("   Algorithm: " + kmf.getAlgorithm());
			System.out.println("   Provider: " + kmf.getProvider());

			// KeyStore types: JKS
			String ksName = "herong.jks";
			char ksPass[] = "HerongJKS".toCharArray();
			char ctPass[] = "My1stKey".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), ksPass);
			System.out.println("\nKeyStore class: " + ks.getClass());
			System.out.println("   Type: " + ks.getType());
			System.out.println("   Provider: " + ks.getProvider());
			System.out.println("   Size: " + ks.size());

			// Generating KeyManager list
			kmf.init(ks, ctPass);
			KeyManager[] kmList = kmf.getKeyManagers();
			System.out.println("\nKeyManager class: " + kmList[0].getClass());
			System.out.println("   # of key manager: " + kmList.length);

			// Generating SSLServerSocketFactory
			sc.init(kmList, null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			System.out.println("\nSSLServerSocketFactory class: "
					+ ssf.getClass());

			// Genearting SSLServerSocket
			SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket();
			System.out.println("\nSSLServerSocket class: " + ss.getClass());
			System.out.println("   String: " + ss.toString());

			// Generating SSLSocketFactory
			sc.init(kmList, null, null);
			SSLSocketFactory sf = sc.getSocketFactory();
			System.out.println("\nSSLSocketFactory class: " + sf.getClass());

			// Genearting SSLSocket
			SSLSocket s = (SSLSocket) sf.createSocket();
			System.out.println("\nSSLSocket class: " + s.getClass());
			System.out.println("   String: " + s.toString());
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

}
