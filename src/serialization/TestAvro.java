package serialization;

import java.io.File;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import example.avro.User;

public class TestAvro {

	public static void main(String[] args) throws Exception {
		User user2 = new User("Ben", 7, "red");
		
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);		
		
		long start = System.currentTimeMillis();
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		File file = new File("users1.avro");
		dataFileWriter.create(user2.getSchema(),file );
		dataFileWriter.append(user2);
		dataFileWriter.append(user2);
		
		
		long eventsToSerialize = 10000000;
		
		for(int i=0;i<eventsToSerialize;i++){
			dataFileWriter.append(user2);
		}
		dataFileWriter.close();
		
		long time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms to serialize at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 
		
		
		// Deserialize Users from disk
		start = System.currentTimeMillis();
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
		User user = null;
		while (dataFileReader.hasNext()) {
		// Reuse user object by passing it to next(). This saves us from
		// allocating and garbage collecting many objects for files with
		// many items.
			user = dataFileReader.next(user);
		}
		dataFileReader.close();
		
		time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms to deserialize at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 

	}

}
