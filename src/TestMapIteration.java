import java.util.Map;
import java.util.Map.Entry;

public class TestMapIteration {

	public static void main(String[] args) {
		Map<String, Integer> users = Map.of("Atul",36, "Anjana", 36);
		for(Entry<String, Integer> entry : users.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
	}
}
