import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class A1CodeJam {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int n = Integer.parseInt(args[0]);
		String fileName = args[1];
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		Map<String, Integer> map = new TreeMap<String, Integer>();
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] words = line.toLowerCase().split(" ");
			for (String word : words) {
				String sanitized = sanitize(word);
				Integer in = map.get(sanitized);
				if (in == null) {
					in = 0;
				}
				in++;
				map.put(sanitized, in);
			}
		}
		br.close();
		
		@SuppressWarnings("unchecked")
		Map<Integer, String> rev = new TreeMap<Integer, String>(new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				return o2.toString().compareTo(o1.toString());
			}
			
		});
		for(String key : map.keySet()){
			Integer val = map.get(key);
			String existing = rev.get(val);
			if(existing != null){
				rev.put(val, existing + "," + key);
			}else{
				rev.put(val,key);
			}
		}
		
		
		System.out.println(rev);

		int count=1;
		for(Integer key : rev.keySet()){						
			if(count++ == n){
				System.out.print(rev.get(key));
			}
		}
	}

	private static String sanitize(String input) {
		input = input.trim();
		if (input.endsWith(".")) {
			input = input.substring(0, input.length() - 1);
		}
		return input;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
