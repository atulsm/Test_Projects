import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.sentinel.security.KeyStoreFactory;
import com.sentinel.security.KeyStoreHandler;
import com.sentinel.security.ProxyX509TrustManager;
import com.sentinel.security.SecurityContext;
import com.sentinel.security.SecurityContextFactory;

public class TestDisabledAlgotithms {

	public static void main(String[] args) throws Exception {
		// Security.setProperty("jdk.tls.disabledAlgorithms", "");
		System.out.printf("%s %s jdk.tls.disabledAlgorithms=%s%n",System.getProperty("java.version", "?"),System.getProperty("java.vendor", "?"),Security.getProperty("jdk.tls.disabledAlgorithms"));
		//SSLSocket s = (SSLSocket) SSLSocketFactory.getDefault().createSocket("164.99.175.156", 8443);
		
		SecurityContext context = SecurityContextFactory.createSecurityContext("TLS",null,null,null,null,true);
		SSLSocket s = (SSLSocket)context.getSecurityContext().getSocketFactory().createSocket("164.99.175.156", 8443);
		
		System.out.printf("Default Protocols, enabled: %s supported: %s%n",	Arrays.toString(s.getEnabledProtocols()),Arrays.toString(s.getSupportedProtocols()));

		s.setEnabledProtocols(new String[] { "TLSv1.1","TLSv1.2" });
		System.out.printf("Set TLSv1, enabled: %s%nNow handshaking...%n",
				Arrays.toString(s.getEnabledProtocols()));
		
		/*
		SSLContext sslContext;
		String keystorePass = System.getProperty(KeyStoreFactory.JAVA_KEYSTORE_PASS);
		KeyStoreHandler ksHandler = KeyStoreFactory.createKeyStoreHandler(null, null);
		
		ProxyX509TrustManager proxyTrustManager = new ProxyX509TrustManager(ksHandler);			
		proxyTrustManager.setCACertParentTrustManagers();
		try {
			sslContext = SSLContext.getInstance( "TLS");
			sslContext.init( null, new TrustManager[] { proxyTrustManager }, new SecureRandom());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/

		
		
		s.startHandshake();
		System.out.println("Ciper " + s.getSession().getCipherSuite() + " ("+ s.getSession().getProtocol() + ")");

	}

}
