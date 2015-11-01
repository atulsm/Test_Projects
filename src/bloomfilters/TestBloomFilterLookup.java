package bloomfilters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.BitSet;

public class TestBloomFilterLookup {

	public static void main(String[] args) throws Exception {
		final int size = Integer.MAX_VALUE;
		
		//load the hashtable		
		BufferedReader reader = new BufferedReader(new FileReader(new File("randomstrings")));
		BitSet bitset = new BitSet(size);
		String val;
		while( (val=reader.readLine()) != null){
			bitset.set(Math.abs(val.hashCode()));
		}
		reader.close();
		
		//try lookups
		long start = System.currentTimeMillis();
		int counter = 0;
		for(int i=0;i<10000;i++){
			String rand = RandomStringGenerator.generate(10);
			if(bitset.get(Math.abs(rand.hashCode()%size))){
				System.out.println("Found " + rand);
				counter ++;
			}
		}
		
		System.out.println("Took " + (System.currentTimeMillis() - start) + " ms for " + counter + " hits");

	}

}
