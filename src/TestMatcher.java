import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestMatcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Pattern pattern = Pattern.compile("access[0-9]*.log");
		
		FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name)
	        {	  
	        	
	        	File f = new File(dir,name);
	            if (!f.isDirectory())
	            {
	                Matcher matcher = pattern.matcher(name);
	                System.out.println(name +":" + matcher.matches());
	                return matcher.matches();
	            }
	            return false;
	        }
		};
		
		File files[] = new File("C:\\Program Files\\Apache Software Foundation\\Apache2.2\\logs").listFiles(filter);

	}

}
