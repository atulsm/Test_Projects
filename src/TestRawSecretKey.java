import java.security.Provider;
import java.security.Security;

import esecurity.base.security.SecurityHandler;


public class TestRawSecretKey {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			String str = SecurityHandler.decryptHardKey("ZL4PTqJwp9GsyW/yn+OqzA==");
			System.out.println(str);
			
			Provider prov = Security.getProvider("SunJSSE");
			prov.remove("SSLContext.SSL");
			prov.remove("SSLContext.SSLv3");
			Security.removeProvider("SunJSSE");
			Security.addProvider(prov);
			
			printProviders();

			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private static void printProviders() {
        // List all of the providers that remain
        for (Provider prov : Security.getProviders()) {
            System.out.println(prov.getName() + prov.getInfo() + prov.getServices());
        }
    }

}
