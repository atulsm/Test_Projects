package serialization;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class TestKryo {

	public static void main(String[] args) throws Exception {
		testLoadKryoSerialize();
	}

	public static void testLoadKryoSerialize() throws Exception{	
		UserPojo pojo = new UserPojo("Ben", 7, "red");
				
		long start = System.currentTimeMillis();
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.register(UserPojo.class);
		//Output output = new Output(new ByteArrayOutputStream());
		Output output = new Output(new FileOutputStream("kryotest.out"));
		
		long eventsToSerialize = 10000000;
		
		for(int i=0;i<eventsToSerialize;i++){
			kryo.writeObject(output, pojo);
		}
		
		output.close();
		long time = (System.currentTimeMillis() - start);
		System.out.println("Took " + time + " ms at the rate of " + ((eventsToSerialize*1000)/time) + " eps"); 
	}
	
	private final static class UserPojo {		
		public  UserPojo(CharSequence name, Integer favorite_number, CharSequence favorite_color){
			this.name = name;
			this.favorite_color=favorite_color;
			this.favorite_number = favorite_number;
		}

		private final CharSequence name;
		private final Integer favorite_number;
		private final CharSequence favorite_color;
	}
}
