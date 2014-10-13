import java.security.AlgorithmParameters;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class TestAES {
	public static void main(String[] args) throws Exception {
		Key a2_ = new SecretKeySpec(
				new byte[]{-28, 127, 24, -105, -90, 67, -51, 1, 17, 73, -39, 108, 83, 42, -49, 73},
				0,
				16,
				"AES");
		BASE64Encoder b64 = new BASE64Encoder();
		System.out.println(b64.encode(a2_.getEncoded()));
		System.out.println(a2_.getAlgorithm());
		System.out.println(a2_.getFormat());
		
		//Create a new secret key in NSS DB with alias "SentinelSecretKey"
    	KeyGenerator kg = KeyGenerator.getInstance("DESede");
        kg.init(112);
        SecretKey skey = kg.generateKey();
       	//ks.store(null, password.toCharArray());
       	
		String out = null;
		String algType = "DESede";
		
		// Encrypt the string using the key
		Cipher ciph = Cipher.getInstance(algType);
		ciph.init(Cipher.ENCRYPT_MODE, skey);
		byte[] enc = ciph.doFinal("novell".getBytes());
		System.out.println(new String(enc));
		
		Cipher ciph1 = Cipher.getInstance(algType);
		AlgorithmParameters params = ciph.getParameters();
		if(params!=null){
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			ciph.init(Cipher.DECRYPT_MODE, skey,new IvParameterSpec(iv));
		}else{
			ciph.init(Cipher.DECRYPT_MODE, skey);			
		}

		System.out.println(new String(ciph.doFinal(enc)));

	}
}
