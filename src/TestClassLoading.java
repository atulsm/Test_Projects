import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;


public class TestClassLoading {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Class cls = Class.forName("org.codehaus.jettison.json.JSONException");
		if(cls !=null){
			//get the name of the jar which is loaded.
			ProtectionDomain pDomain = cls.getProtectionDomain();
			CodeSource cSource = pDomain.getCodeSource();
			URL loc = cSource.getLocation();
			File jarFile = new File(loc.getFile());
			final String fileName = jarFile.getName();
			System.out.println(jarFile);
			System.getProperty("java.class.path");
		}
	}

}
