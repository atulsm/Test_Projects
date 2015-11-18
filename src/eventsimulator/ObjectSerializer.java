package eventsimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.netiq.sentinel.cdh.SentinelEvent;

/**
 * Serialize map to bytes and back
 * 
 * @author SAtul
 *
 */
public class ObjectSerializer {

	public static byte[] getBytes(Object event) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os;
		try {			
			os = new ObjectOutputStream(out);
		    os.writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return out.toByteArray();
	}

	public static Object getEvent(byte[] bytes) {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	    ObjectInputStream is;
		try {
			is = new ObjectInputStream(in);
		    return is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
