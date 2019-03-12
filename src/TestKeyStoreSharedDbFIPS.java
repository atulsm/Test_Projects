import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.security.provider.Sun;
import sun.security.rsa.SunRsaSign;

/**
 * Before running the tool run the following in your Linux box.
 * 
	keytool -genkey -alias webserver -keyalg RSA -keystore /tmp/KeyStore.jks -keysize 2048
		#(Provide keystore password as changeit)
	
	cd /tmp
	export LD_LIBRARY_PATH=/usr/lib64
	export NSS_DEFAULT_DB_TYPE=sql
	rm -rf mkdir /tmp/fips/
	mkdir /tmp/fips/
	mkdir /tmp/fips/nssdb
	modutil -create -dbdir sql:/tmp/fips/nssdb/ -force
	modutil -fips true -dbdir sql:/tmp/fips/nssdb -force
	touch /tmp/fips/nssdb/secmod.db
	modutil -changepw "NSS FIPS 140-2 Certificate DB" -dbdir sql:/tmp/fips/nssdb -force
		# (provide password as password1!)
	java -Dnss.lib=/usr/lib64 -Dnss.db=/tmp/fips/nssdb  -Djavax.net.ssl.keyStorePassword=password1! TestKeyStoreSharedDbFIPS /tmp/KeyStore.jks changeit	
      
 * @author satul
 *
 */
public class TestKeyStoreSharedDbFIPS {
	
    public static final String NSS_LIB_DIR_PROP = "nss.lib";
    public static final String NSS_DB_DIR_PROP = "nss.db";
    public static final String SUN_JSSE = "SunJSSE";
    public static List<String> disabledAlgs = new ArrayList<String>();

    private static final Logger logger = Logger.getLogger(TestKeyStoreSharedDbFIPS.class.getName());
    
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.out.println("Usage eg: java -Dnss.lib=/usr/lib64 -Dnss.db=/tmp/fips/nssdb  -Djavax.net.ssl.keyStorePassword=password1! TestKeyStoreSharedDbFIPS /tmp/jre8/lib/security/cacerts changeit");
			System.exit(1);
		}
		
        enablePkcs11Jsse(System.getProperty(NSS_LIB_DIR_PROP), System.getProperty(NSS_DB_DIR_PROP));
        testFips();

		String file = args[0];
		char[] keystorePassword = args[1].toCharArray();
		FileInputStream keystoreStream = new FileInputStream(file);

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(keystoreStream, keystorePassword);

		Enumeration<String> aliases = keyStore.aliases();
		
		while(aliases.hasMoreElements()){
			String alias = aliases.nextElement();
			System.out.println(alias + " : " + keyStore.getCertificate(alias).getType());
			
		}

	}
	
	private static void testFips(){
		String keyPass = System.getProperty("javax.net.ssl.keyStorePassword");		
        KeyStore store;

        try {
            store = KeyStore.getInstance("PKCS11");
            if (keyPass != null) {
                store.load(null, keyPass.toCharArray());
            } else {
                store.load(null, null);
            }
            System.out.println("FIPS test success");
        } catch (Throwable e) {
            e.printStackTrace();
            store = null;
            System.out.println("FIPS test failed");
        }        				
	}
	
    /**
     * Configures a PKCS11 based provider and replace the existing JSSE provider
     * with one configured against the newly added PKCS11 based provider.
     */
    public static void enablePkcs11Jsse( String libDir, String dbDir) throws Exception {
    	removeAllProviders();
    	
        Provider nss = getNSSFIPSProvider( libDir, dbDir);
        removeDisabledAlgos(nss);   	
        Security.insertProviderAt(nss, 1);

        Provider sunJsse = new com.sun.net.ssl.internal.ssl.Provider(nss);
        removeDisabledAlgos(sunJsse);
        Security.insertProviderAt(sunJsse,2);
        
        Sun sun = new Sun();
        removeDisabledAlgos(sun);   	
        Security.insertProviderAt(sun,3);
        
        SunRsaSign sunrsa = new SunRsaSign();
        removeDisabledAlgos(sunrsa);
        Security.insertProviderAt(sunrsa,4);
    }	
    
    
    /**
    * Loads and returns an instance of the NSS provider in FIPS mode
    *
    * @return
    * @throws IOException
    */
   private static Provider getNSSFIPSProvider( String libDir, String dbDir) throws Exception {
       
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
   
   /**
    * Remove all default providers except Sun and SunRsaSign
    */
   private static void removeAllProviders(){  	
   	Provider[] providers = Security.getProviders();
   	for(Provider prov : providers){
   		Security.removeProvider(prov.getName());
   	}
   }
   	
    /**
     * Remove invalid algorithms
     * @param provider
     */
    private static void removeDisabledAlgos(Provider provider){    	
    	for(String alg : disabledAlgs){
    		if(provider.getProperty(alg) != null){
    			logger.info("Removing algorithm " + alg + " from provider " + provider);
        		provider.remove(alg);
    		}
    	}
    }   

}
