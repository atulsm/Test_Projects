package bloomfilters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;

/*
Found opueyqyeyi
Found irpipwpewr
Found puioutwrrq
Found iteirqripe
Found ytwuiyropp
Found tqriyywwyi
Found trqttttwyo
Found rrryrpouoe
Took 20 ms for 8 hits
*/
public class TestHashmapLookup {
	public static void main(String[] args) throws Exception {		
		//load the hashtable		
		BufferedReader reader = new BufferedReader(new FileReader(new File("randomstrings")));
		Hashtable<String, Boolean> hashtable = new Hashtable<String, Boolean>();
		String val;
		while( (val=reader.readLine()) != null){
			hashtable.put(val, Boolean.TRUE);
		}
		reader.close();
		
		//try lookups
		long start = System.currentTimeMillis();
		int counter = 0;
		for(int i=0;i<10000;i++){
			String rand = RandomStringGenerator.generate(10);
			if(hashtable.containsKey(rand)){
				System.out.println("Found " + rand);
				counter ++;
			}
		}
		
		System.out.println("Took " + (System.currentTimeMillis() - start) + " ms for " + counter + " hits");

	}

}
