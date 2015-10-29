import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;

import javax.crypto.Cipher;

import com.sun.crypto.provider.SunJCE;


public class TestSecurityProvider {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Security.removeProvider("SunJCE");
		long start = System.nanoTime();
		boolean test = true;
		if(test){
			Provider prov = Security.getProvider("SUN");
			Service ser = prov.getService("KeyStore", "jks");
			System.out.println(ser);
			
			SunJCE sunJce = new SunJCE();
			Object obj = sunJce.get("Cipher.DES/CBC/PKCS5Padding");
			Object obj1 =  sunJce.getProperty("Cipher.AES");
			//AESCipher aes = new AESCipher();
			//System.out.println(sunJce.getServices());

			sunJce.clear();
			sunJce.put("Cipher.AES",obj);
			//System.out.println(sunJce.getServices());
			Security.addProvider(sunJce);
			Cipher cip = Cipher.getInstance("AES");
			Security.removeProvider("SunJCE");
		}else{
			Cipher cip = Cipher.getInstance("AES");
		}

		long end = System.nanoTime();
		
		System.out.println((end-start)/1000000); //200 millisec		

	}

}
