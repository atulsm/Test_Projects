package functional.programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestWordCount {

	public static void main(String[] args) {
		String input = "The reduce() method takes a BinaryOperator as parameter, which can easily be implemented using a lambda expression. "
				+ "The BinaryOperator.apply() method is the method implemented by the lambda expression above. "
				+ "This method takes two parameters. The acc which is the accumulated value, and item which is an element from the stream. "
				+ "Thus, the value created by the reduce() function is the accumulated value after processing the last element in the stream. "
				+ "In the example above, each item is concatenated to the accumulated value. This is done by the lambda expression "
				+ "implementing the BinaryOperator";
		
		Map<String,Integer> response = Arrays.asList(input.split(" "))
			.stream()
			.flatMap((s) -> {  //Sanitize the stream to contain only strings
				char[] chars = s.toCharArray();
				for(int i=0;i<chars.length;i++) {
					char c = chars[i];
					if(!Character.isAlphabetic(c)) {
						chars[i] = ' ';
					}
				}
				 //Necessary to return the stream since it can contain multiple strings after sanitization
				return Arrays.asList(new String(chars).trim().split(" ")).stream();
			})
			.map((s) -> Map.of(s,1)) //Initializing every word with count 1
			.reduce(new HashMap<String, Integer>(), (acc, item) -> {
				Entry<String, Integer> entry = item.entrySet().iterator().next();

				int count=0;
				if(acc.containsKey(entry.getKey())) {
					count = acc.get(entry.getKey());
				}

				acc.put(entry.getKey(), count+entry.getValue());
				return acc;
			});
		System.out.println(response);
	}

}

/*
String[] data = new String[1];
data[0] = s;
data[1] = "1";
return data;
*/