import java.util.HashMap;

import com.sun.tools.jdi.LinkedHashMap;


public class TestHashmapjava8 {

	public static void main(String[] args) {
		HashMap map = new HashMap<>();
		map.put("1", "EventSourceManagers");
		map.put("2", "SentinelHosts");
		System.out.println(map);		
	}

	public static void main1(String[] args) {
		LinkedHashMap map = new LinkedHashMap();
		map.put("SentinelHosts", "SentinelHosts");
		map.put("EventSourceManagers", "EventSourceManagers");

		System.out.println(map);		
	}
}
