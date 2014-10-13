import java.util.Iterator;
import java.util.Properties;


public class TestSystemProperty {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties prop = System.getProperties();
		for(Iterator<Object> itr = prop.keySet().iterator();itr.hasNext();){
			String key = (String)itr.next();
			String value = prop.getProperty(key);
			System.out.println(key + " : " + value);
		}

	}

}
