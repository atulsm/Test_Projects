import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SampleSSLClient {

	  
    public static void main(String[] args) {
     
        try{
            //no keystore required, just verifying the servers cert
            //set necessary truststore properties - using JKS
      //      System.setProperty("javax.net.ssl.trustStore", "truststoreCA.jks");
       //     System.setProperty("javax.net.ssl.trustStorePassword", "pleas3w0rk");
        //    System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
          
        	SSLContext sslContext = SSLContext.getInstance("SSLv3");       	
        	KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
        	keyMgrFactory.init(null,null);
        	sslContext.init(keyMgrFactory.getKeyManagers(), TRUST_ALL_MANAGER, new SecureRandom());

        	
            //connect to google          
            SSLSocketFactory factory = (SSLSocketFactory) sslContext.getSocketFactory();
            SSLSocket sslSock = (SSLSocket) factory.createSocket("164.99.135.40",10013);            
            
            sslSock.startHandshake();
            System.out.println(sslSock.getEnableSessionCreation());            
            System.out.println("SSL Handshake success, sleeping for 15 secs before renogiation");
            Thread.sleep(1500);            
            
            sslSock.startHandshake();
            System.out.println(sslSock.getEnableSessionCreation());
            System.out.println("2nd SSL Handshake success, sleeping for 15 sec before renogiation");
            Thread.sleep(1500);

            sslSock.startHandshake();
            System.out.println(sslSock.getEnableSessionCreation());
            System.out.println("3nd SSL Handshake success, sleeping for 15 before renogiation");

          
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    
    // Trust all certs
    private static final TrustManager[] TRUST_ALL_MANAGER = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }
            }
    };
}