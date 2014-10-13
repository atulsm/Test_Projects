import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class PKCS11Test {

    private static final Logger logger = Logger.getLogger(PKCS11Test.class.getName());

    public static final String NSS_LIB_DIR_PROP = "nssLib";
    public static final String NSS_DB_DIR_PROP = "nssDb";
    public static final String SUN_JSSE = "SunJSSE";
    public static final String keyStorePassword = "Password1!"; 
    
    private static final int PORT = 2222;
    private static KeyStore ks;
    
    private KeyManager[] keyManager;
    private TrustManager[] trustManager;

    public static void main(String args[]) {
        try {
        	final PKCS11Test test = new PKCS11Test();
        	test.enablePkcs11Jsse();
        	ks = test.getKeyStore();      
        	
        	new Thread(){
        		@Override
        		public void run() {
        			try{
        				Thread.currentThread().sleep(1000);
        				
        				SSLContext sslContext = test.getSecurityContext();
        				SSLSocketFactory factory = sslContext.getSocketFactory();
        				SSLSocket clientSoc = (SSLSocket)factory.createSocket("localhost", PORT);
        				clientSoc.startHandshake();
        				
        			}catch (Exception e) {
						e.printStackTrace();
					}
        			
        		}
        	}.start();

			ServerSocket server = new ServerSocket(PORT);
			Socket skt = server.accept();
        	
        	SSLContext sslContext = test.getSecurityContext();
			SSLSocketFactory factory = sslContext.getSocketFactory();
			SSLSocket sslSocket = (SSLSocket) factory.createSocket(skt, skt.getLocalAddress().toString(), skt.getPort(),false);
        	
			sslSocket.setUseClientMode(false);
			sslSocket.setNeedClientAuth(false);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    /**
     * Configures a PKCS11 based provider and replace the existing JSSE provider
     * with one configured against the newly added PKCS11 based provider.
     */
    public void enablePkcs11Jsse() throws Exception {
        Provider nss = getNSSFIPSProvider();
        Security.insertProviderAt(nss, 1);

        int sunJssePosition = -1;
        int currentIndex = 0;
        for (Provider provider : Security.getProviders()) {
            if (SUN_JSSE.equals(provider.getName())) {
                sunJssePosition = currentIndex + 1;
                break;
            }
            currentIndex++;
        }

        Security.removeProvider(SUN_JSSE);

        Provider sunJsse = new com.sun.net.ssl.internal.ssl.Provider(nss);
        if (sunJssePosition == -1) {
            Security.addProvider(sunJsse);
        } else {
            Security.insertProviderAt(sunJsse, 2);
        }
    }

    /**
     * Loads and returns an instance of the NSS provider in FIPS mode
     *
     * @return
     * @throws IOException
     */
    private Provider getNSSFIPSProvider() throws Exception {
        String libDir = System.getProperty(NSS_LIB_DIR_PROP);
        String dbDir = System.getProperty(NSS_DB_DIR_PROP);
        
        if(libDir == null || dbDir == null) {
            throw new Exception(NSS_LIB_DIR_PROP + " or " + NSS_DB_DIR_PROP + " not set.");
        }

        Properties props = new Properties();
        props.put("name", "NSSfips");
        props.put("nssLibraryDirectory", libDir);
        props.put("nssSecmodDirectory", dbDir);
        props.put("nssModule", "fips");
        props.put("nssDbMode", "readWrite");

        return createProvider(props);
    }

    private static Provider createProvider(Properties props) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        props.store(out, null);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        Provider ret = new sun.security.pkcs11.SunPKCS11(in);
        if (logger.isLoggable(Level.FINE)) {
            // Log all of the registered services
            for (Map.Entry<Object, Object> entry : ret.entrySet()) {
                logger.log(Level.FINE, "{0} = {1}", new Object[]{entry.getKey(), entry.getValue()});
            }
        }
        return ret;
    }

    public KeyStore getKeyStore() {
        KeyStore store;

        try {
            store = KeyStore.getInstance("PKCS11");
            if (keyStorePassword != null) {
                store.load(null, keyStorePassword.toCharArray());
            } else {
                store.load(null, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            store = null;
        }
        return store;
    }
    
    private void init() throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        trustManager = tmf.getTrustManagers();

        KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
        keyMgrFactory.init(ks, keyStorePassword.toCharArray());
        keyManager = keyMgrFactory.getKeyManagers();
    }

    public SSLContext getSecurityContext() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        init();

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keyManager, trustManager, null);

        logger.log(Level.FINE, "SSL initialization complete.");
        return sslcontext;

    }
}
