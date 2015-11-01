package bloomfilters;

import java.io.File;
import java.io.FileWriter;


public class PopulateRandomStringFile {

	public static void main(String[] args) throws Exception{
		File file = new File("randomstrings");
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		
		for(int i=0;i< 10000000;i++){
			writer.write(RandomStringGenerator.generate(10)+"\n");
		}
		
		writer.close();
	}

}
