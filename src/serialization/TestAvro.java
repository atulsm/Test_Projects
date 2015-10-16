package serialization;

import java.io.File;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import example.avro.User;

public class TestAvro {

	public static void main(String[] args) throws Exception {
		User user2 = new User("Ben", 7, "red");
		
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);		
		
		long start = System.currentTimeMillis();
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		dataFileWriter.create(user2.getSchema(), new File("users1.avro"));
		dataFileWriter.append(user2);
		dataFileWriter.append(user2);
		
		
		long eventsToSerialize = 10000000;
		
		for(int i=0;i<eventsToSerialize;i++){
			dataFileWriter.append(user2);
		}
		dataFileWriter.close();
		
		long time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 
		
		


	}

}
