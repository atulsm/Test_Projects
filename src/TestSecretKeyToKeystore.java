import java.security.KeyStore;

import javax.crypto.spec.SecretKeySpec;

public class TestSecretKeyToKeystore {

	/**
	 * Keytool command: keytool -list -keystore server_secret.jceks -storetype JCEKS -storepass “novell” 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		KeyStore ks = KeyStore.getInstance("JCEKS");

		char[] password = "novell".toCharArray();
		java.io.FileInputStream fis = new java.io.FileInputStream(
				"C:\\Documents and Settings\\admin\\Desktop\\desktop_before_demo\\ssl\\server.jks");
		ks.load(null, password);
		fis.close();

		// save my secret key
		javax.crypto.SecretKey mySecretKey;
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
		ks.setEntry("secretKey", skEntry, new KeyStore.PasswordProtection(
				password));

		// store away the keystore
		java.io.FileOutputStream fos = new java.io.FileOutputStream(
				"C:\\Documents and Settings\\admin\\Desktop\\desktop_before_demo\\ssl\\server_secret.jceks");
		ks.store(fos, password);
		fos.close();
		
		System.out.println(ks.getKey("secretKey", password));
	}
	
	private static SecretKeySpec key = new SecretKeySpec(
			new byte[]{-28, 127, 24, -105, -90, 67, -51, 1, 17, 73, -39, 108, 83, 42, -49, 73},
			0,
			16,
			"AES");

}
