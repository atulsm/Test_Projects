package functional.programming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighOrderFunctionsJavaExamples {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("One");
		list.add("Abc");
		list.add("BCD");
		
		Collections.sort(list, (String a, String b) -> a.compareTo(b) );
		System.out.println(list);
	}

}
