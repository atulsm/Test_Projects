import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;

public class TestKeyStoreDelete {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{

		String alias = "webserver";
		char[] keystorePassword = "password".toCharArray();

		FileInputStream keystoreStream = new FileInputStream("d:\\test\\webserver.jks");

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(keystoreStream, keystorePassword);

		Key privateKey = keyStore.getKey(alias, keystorePassword);
		System.out.println(privateKey);
		
		keyStore.deleteEntry(alias);

	}

}
