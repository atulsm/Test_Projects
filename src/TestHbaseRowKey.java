import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.UUID;

import org.apache.hadoop.hbase.util.Bytes;

public class TestHbaseRowKey {

	public static void main(String[] args) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		for(int i=0;i<100;i++){
			String key = "";
			key += "1";
			key += System.currentTimeMillis()/1000;
			//System.out.println(key);
	
			byte[] digest = md.digest(Bytes.toBytes(UUID.randomUUID().toString()));
			ByteBuffer wrapped = ByteBuffer.wrap(digest); // big-endian by default
	
			int wint = wrapped.getInt();
			//System.out.println(wint);
			//System.out.println(Bytes.toBytes(wint).length);
			
			int num = Math.abs(wrapped.getShort()%10000);
			String formatted = String.format("%04d", num);
			System.out.println(formatted);
			
			key += num;
			
			/*
			System.out.println(key);		
			System.out.println(Bytes.toBytes(key).length);
			System.out.println(Bytes.toBytes(Long.valueOf(key).longValue()).length);
			*/
		}
		
	}

}
