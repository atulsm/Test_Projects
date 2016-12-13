import java.nio.ByteBuffer;
import java.security.MessageDigest;

import org.apache.hadoop.hbase.util.Bytes;

public class TestMD5 {

	public static void main(String[] args) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");		
		
		for(int i=0;i<1000000;i++){
			long l = System.currentTimeMillis();
			byte[] digest = md.digest(Bytes.toBytes(l));
	
	
			ByteBuffer wrapped = ByteBuffer.wrap(digest); // big-endian by default
			int num = Math.abs(wrapped.getShort()%10);
			
			System.out.println(num);
			//Thread.sleep(1000);
		}
	}

}
