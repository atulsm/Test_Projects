import java.util.LinkedHashMap;
import java.util.Map;


public class TestLRUCache {
	static int resetCount = 0;
	static int mapsize = 1000;

    private static Map<String, Integer> partitionCache = new LinkedHashMap<String, Integer>(mapsize, .75F, true) {
        public boolean removeEldestEntry(Map.Entry eldest) {
            boolean ret =  size() > mapsize;
            if(ret){
            	resetCount++;
            }
            
            return ret;
        }
    };
	
	public static void main(String[] args) {
		mapsize = 2;
		partitionCache.put("1", 1); //should be removed
		partitionCache.put("2", 2);
		partitionCache.put("3", 3);
		System.out.println(partitionCache);
		
		//testing get
		partitionCache.clear();
		partitionCache.put("1", 1); 
		partitionCache.put("2", 2); //should be removed since 1 is looked up
		partitionCache.get("1");
		partitionCache.put("3", 3);
		System.out.println(partitionCache);

	}
	
	public static void loadTest() {
		System.out.println(partitionCache.get("test"));
		
		for(int i = 0;i<10000;i++){
			partitionCache.put(i+"", i);
		}
		
		System.out.println(resetCount);
		System.out.println(partitionCache.size());
	}

}
