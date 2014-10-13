import java.util.LinkedHashMap;
import java.util.Map;


public class TestLRUCache {
	static int resetCount = 0;

    private static Map<String, Long> partitionCache = new LinkedHashMap<String, Long>(1000, .75F, true) {
        public boolean removeEldestEntry(Map.Entry eldest) {
            boolean ret =  size() > 1000;
            if(ret){
            	resetCount++;
            }
            
            return ret;
        }
    };
	
	public static void main(String[] args) {
		System.out.println(partitionCache.get("test"));
		
		for(long i = 0;i<10000;i++){
			partitionCache.put(i+"", i);
		}
		
		System.out.println(resetCount);
		System.out.println(partitionCache.size());

	}

}
