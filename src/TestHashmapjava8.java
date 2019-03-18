import java.util.HashMap;
import java.util.LinkedHashMap;


public class TestHashmapjava8 {

	public static void main(String[] args) {
		HashMap map = new HashMap<>();
		map.put("1", "EventSourceManagers");
		map.put("2", "SentinelHosts");
		map.put(null,  "Atul");
		System.out.println(map);	
		System.out.println(map.get(null));
	}

	public static void main1(String[] args) {
		LinkedHashMap map = new LinkedHashMap();
		map.put("SentinelHosts", "SentinelHosts");
		map.put("EventSourceManagers", "EventSourceManagers");

		System.out.println(map);		
	}
}
