import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import com.esecurity.util.logging.ExceptionHandler;


public class TestPropertyFileWrite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean test = false;
		
		Properties props = new Properties();
		File file = new File("test.properties");
		try{	
			props.put("key1", "val1");
			props.put("url", "http://localhost:1111");
			
			if(test){
				props.store(new FileOutputStream(file), "");
			}else{
				try (PrintWriter pw = new PrintWriter(file)){			
					for (Iterator<Entry<Object, Object>> itr = props.entrySet().iterator(); itr.hasNext();) {
						Entry<Object, Object> entry = itr.next();
				        pw.println(entry.getKey() + "=" + entry.getValue());
				    }
				} catch (Exception e1) {
					e1.printStackTrace();;
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
