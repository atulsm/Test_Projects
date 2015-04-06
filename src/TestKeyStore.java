import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Enumeration;

public class TestKeyStore {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.out.println("Usage: java TestKeyStore FILE PASSWORD");
			System.exit(1);
		}
		
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

}
