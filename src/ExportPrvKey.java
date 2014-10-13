import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;

public class ExportPrvKey {

	public static void main(String[] args) {

		if (args.length < 3) {
			System.out
					.println("usage: java ExportPrvKey keystore password alias");
		}
		try {
			KeyStore ks = KeyStore.getInstance("jks");
			ks.load(new FileInputStream(args[0]), args[1].toCharArray());
			Key key = ks.getKey(args[2], args[1].toCharArray());
			System.out.write(key.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
