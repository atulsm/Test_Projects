package serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class TestKryo {

	public static void main(String[] args) throws Exception {
		testLoadKryoSerialize();
	}

	public static void testLoadKryoSerialize() throws Exception{	
		UserPojo pojo = new UserPojo();
		pojo.name = "Ben";
		pojo.favorite_color="red";
		pojo.favorite_number = 7;
				
		long start = System.currentTimeMillis();
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.register(UserPojo.class);
		
		long eventsToSerialize = 10000000;
		File file = new File("kryotest.out");
		
		
		Output output = new Output(new FileOutputStream(file));
		
		
		for(int i=0;i<eventsToSerialize;i++){
			kryo.writeObject(output, pojo);
		}	
		output.flush();
		output.close();
		
		
		
		long time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 
		
		///*
		start = System.currentTimeMillis();
		Input input = new Input(new FileInputStream(file));
		UserPojo someObject = null;
		int i=0;
		try{
			while( (someObject = kryo.readObject(input, UserPojo.class)) != null){ // () != null){			
				i++;
			}
		}catch(Exception e){
			if(i != eventsToSerialize){
				System.out.println(e.getLocalizedMessage());
			}
		}
		input.close();
		
		 time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms to deserialize at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 
		//*/
	}
	
	public static class UserPojo implements Serializable {		
		private String name;
		private String favorite_color;
		private int favorite_number;
	}
}
