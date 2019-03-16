import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestMapIteration {

	public static void main(String[] args) {
		Map<String, Integer> users = Map.of("Atul",36, "Anjana", 36);
		for(Entry<String, Integer> entry : users.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
		Map<String, Integer> mutableUser = new HashMap<String, Integer>(users);
		mutableUser.compute("Atul", (key, value) -> value+10);
		System.out.println(mutableUser);
	}
}
