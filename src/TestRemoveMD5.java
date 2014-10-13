import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.security.provider.Sun;

public class TestRemoveMD5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Security.removeProvider("SUN");
		Sun sun = new Sun();
		sun.remove("MessageDigest.MD5"); //Comment and it will work !!!
		Security.addProvider(sun);
		Cipher ciph = Cipher.getInstance("AES");

		//MessageDigest md = MessageDigest.getInstance("MD2");
		//encrypt("test");				
	}
	
	private static byte[] encrypt(String in) throws Exception {
		Cipher ciph = Cipher.getInstance(key.getAlgorithm());
		ciph.init(Cipher.ENCRYPT_MODE, key);
		return ciph.doFinal(in.getBytes());
	}
	
	private static Key key = new SecretKeySpec(
			new byte[]{45, 17, 64, -5, -0, 6, 1, 34, 46, 8, -91, -8, 32, 21, -9, 30},0,16,"AES");

}
